package ru.otus.otuskotlin.yan.swiftorder.appui.client

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.*
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.*
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import java.io.Closeable
import java.time.Duration
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class KafkaOrderClient(
    private val bootstrapServers: String,
    private val objectMapper: ObjectMapper,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
) : OrderClient, Closeable {

    private val pendingRequests = ConcurrentHashMap<String, CompletableDeferred<String>>()

    private val producer = KafkaProducer<String, String>(Properties().apply {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
    })

    init { startConsumer() }

    private fun startConsumer() {
        scope.launch(Dispatchers.IO) {
            val consumer = KafkaConsumer<String, String>(Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
                put(ConsumerConfig.GROUP_ID_CONFIG, "swift-order-ui-group")
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
            })
            consumer.subscribe(listOf(
                "swift-order-create-response",
                "swift-order-read-response",
                "swift-order-update-response",
                "swift-order-delete-response",
                "swift-order-search-response",
            ))
            try {
                while (isActive) {
                    consumer.poll(Duration.ofMillis(100)).forEach { record ->
                        val correlationId = record.headers()
                            .lastHeader("correlationId")?.value()?.let { String(it) } ?: return@forEach
                        pendingRequests.remove(correlationId)?.complete(record.value())
                    }
                }
            } finally {
                consumer.close()
            }
        }
    }

    private suspend fun sendAndReceive(requestTopic: String, request: Any): String {
        val correlationId = UUID.randomUUID().toString()
        val deferred = CompletableDeferred<String>()
        pendingRequests[correlationId] = deferred

        val record = ProducerRecord<String, String>(requestTopic, objectMapper.writeValueAsString(request))
        record.headers().add("correlationId", correlationId.toByteArray())
        producer.send(record)

        return try {
            withTimeout(5_000) { deferred.await() }
        } catch (e: TimeoutCancellationException) {
            pendingRequests.remove(correlationId)
            throw OrderClientException("Kafka timeout after 5s for topic $requestTopic", e)
        }
    }

    override suspend fun search(): List<SwiftOrder> {
        val json = sendAndReceive("swift-order-search-request", OrderSearchRequest())
        val response = objectMapper.readValue(json, OrderSearchResponse::class.java)
        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Search failed")
        return response.orders?.map { it.toSwiftOrder() } ?: emptyList()
    }

    override suspend fun read(id: String): SwiftOrder {
        val json = sendAndReceive("swift-order-read-request", OrderReadRequest(order = OrderReadObject(id = id)))
        val response = objectMapper.readValue(json, OrderReadResponse::class.java)
        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Read failed")
        return response.order?.toSwiftOrder() ?: throw OrderClientException("Order not found")
    }

    override suspend fun create(order: SwiftOrder): SwiftOrder {
        val json = sendAndReceive("swift-order-create-request", order.toCreateRequest())
        val response = objectMapper.readValue(json, OrderCreateResponse::class.java)
        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Create failed")
        return response.order?.toSwiftOrder() ?: throw OrderClientException("No order in response")
    }

    override suspend fun update(order: SwiftOrder): SwiftOrder {
        val json = sendAndReceive("swift-order-update-request", order.toUpdateRequest())
        val response = objectMapper.readValue(json, OrderUpdateResponse::class.java)
        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Update failed")
        return response.order?.toSwiftOrder() ?: throw OrderClientException("No order in response")
    }

    override suspend fun delete(id: String) {
        val json = sendAndReceive("swift-order-delete-request",
            OrderDeleteRequest(order = OrderDeleteObject(id = id)))
        val response = objectMapper.readValue(json, OrderDeleteResponse::class.java)
        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Delete failed")
    }

    override fun close() {
        scope.cancel()
        producer.close()
    }
}
