package ru.otus.otuskotlin.yan.swiftorder.appkafka

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.support.Acknowledgment
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateResponse
import ru.otus.otuskotlin.yan.swiftorder.appcommon.AppSettings
import ru.otus.otuskotlin.yan.swiftorder.appcommon.controllerHelper
import ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers.fromTransport
import ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers.toTransportCreate
import ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers.toTransportDelete
import ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers.toTransportRead
import ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers.toTransportSearch
import ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers.toTransportUpdate
import ru.otus.otuskotlin.yan.swiftorder.appkafka.config.ORDER_CONSUMER_CONTAINER_FACTORY
import ru.otus.otuskotlin.yan.swiftorder.appkafka.config.SwiftOrderTopicsProperties
import java.time.Duration
import java.util.UUID

class OrderKafkaConsumer(
    @Suppress("unused")
    containerFactory: ConcurrentKafkaListenerContainerFactory<String, String>,
    private val props: SwiftOrderTopicsProperties,
    private val appSettings: AppSettings,
    private val objectMapper: ObjectMapper,
    private val producer: OrderKafkaProducer,
) {
    private val log = appSettings.loggerProvider.logger(OrderKafkaConsumer::class)
    private val batchRedeliverMs = 3_000L

    @KafkaListener(
        id = "swift-order-create-listener",
        topics = ["#{swiftOrderTopicsProperties.createRequestTopic.name}"],
        groupId = "swift-order-group",
        containerFactory = ORDER_CONSUMER_CONTAINER_FACTORY,
    )
    fun handleCreate(records: ConsumerRecords<String?, String?>, ack: Acknowledgment) =
        acceptBatch(records, ack) { message ->
            val request = objectMapper.readValue(message, OrderCreateRequest::class.java)
            val response: OrderCreateResponse = appSettings.controllerHelper(
                { fromTransport(request) },
                { toTransportCreate() },
                OrderKafkaConsumer::class,
                "create",
            )
            producer.send(props.createResponseTopic.name, objectMapper.writeValueAsString(response))
        }

    @KafkaListener(
        id = "swift-order-read-listener",
        topics = ["#{swiftOrderTopicsProperties.readRequestTopic.name}"],
        groupId = "swift-order-group",
        containerFactory = ORDER_CONSUMER_CONTAINER_FACTORY,
    )
    fun handleRead(records: ConsumerRecords<String?, String?>, ack: Acknowledgment) =
        acceptBatch(records, ack) { message ->
            val request = objectMapper.readValue(message, OrderReadRequest::class.java)
            val response: OrderReadResponse = appSettings.controllerHelper(
                { fromTransport(request) },
                { toTransportRead() },
                OrderKafkaConsumer::class,
                "read",
            )
            producer.send(props.readResponseTopic.name, objectMapper.writeValueAsString(response))
        }

    @KafkaListener(
        id = "swift-order-update-listener",
        topics = ["#{swiftOrderTopicsProperties.updateRequestTopic.name}"],
        groupId = "swift-order-group",
        containerFactory = ORDER_CONSUMER_CONTAINER_FACTORY,
    )
    fun handleUpdate(records: ConsumerRecords<String?, String?>, ack: Acknowledgment) =
        acceptBatch(records, ack) { message ->
            val request = objectMapper.readValue(message, OrderUpdateRequest::class.java)
            val response: OrderUpdateResponse = appSettings.controllerHelper(
                { fromTransport(request) },
                { toTransportUpdate() },
                OrderKafkaConsumer::class,
                "update",
            )
            producer.send(props.updateResponseTopic.name, objectMapper.writeValueAsString(response))
        }

    @KafkaListener(
        id = "swift-order-delete-listener",
        topics = ["#{swiftOrderTopicsProperties.deleteRequestTopic.name}"],
        groupId = "swift-order-group",
        containerFactory = ORDER_CONSUMER_CONTAINER_FACTORY,
    )
    fun handleDelete(records: ConsumerRecords<String?, String?>, ack: Acknowledgment) =
        acceptBatch(records, ack) { message ->
            val request = objectMapper.readValue(message, OrderDeleteRequest::class.java)
            val response: OrderDeleteResponse = appSettings.controllerHelper(
                { fromTransport(request) },
                { toTransportDelete() },
                OrderKafkaConsumer::class,
                "delete",
            )
            producer.send(props.deleteResponseTopic.name, objectMapper.writeValueAsString(response))
        }

    @KafkaListener(
        id = "swift-order-search-listener",
        topics = ["#{swiftOrderTopicsProperties.searchRequestTopic.name}"],
        groupId = "swift-order-group",
        containerFactory = ORDER_CONSUMER_CONTAINER_FACTORY,
    )
    fun handleSearch(records: ConsumerRecords<String?, String?>, ack: Acknowledgment) =
        acceptBatch(records, ack) { message ->
            val request = objectMapper.readValue(message, OrderSearchRequest::class.java)
            val response: OrderSearchResponse = appSettings.controllerHelper(
                { fromTransport(request) },
                { toTransportSearch() },
                OrderKafkaConsumer::class,
                "search",
            )
            producer.send(props.searchResponseTopic.name, objectMapper.writeValueAsString(response))
        }

    private fun acceptBatch(
        records: ConsumerRecords<String?, String?>,
        ack: Acknowledgment,
        handler: suspend (String) -> Unit,
    ) {
        val batch = records.toList()
        val lastOffsets = getLastOffsetsByPartition(batch)

        log.info("Processing batch of ${batch.size} records. Last offsets by partition: $lastOffsets")

        try {
            runBlocking {
                batch
                    .groupBy { it.key() ?: UUID.randomUUID().toString() }
                    .values
                    .map { group ->
                        async(Dispatchers.Default) {
                            for (record in group) {
                                record.value()?.let { value ->
                                    try {
                                        handler(value)
                                    } catch (e: Exception) {
                                        if (isRetryable(e)) {
                                            throw e
                                        }
                                        log.error(
                                            msg = "Non-retryable error on record key=${record.key()} " +
                                                "topic=${record.topic()} partition=${record.partition()} " +
                                                "offset=${record.offset()}. Skipping.",
                                            e = e,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    .awaitAll()
            }
            ack.acknowledge()
        } catch (e: Exception) {
            log.error(
                msg = "Retryable error processing batch. Redelivering in ${batchRedeliverMs}ms. Last offsets: $lastOffsets",
                e = e,
            )
            ack.nack(0, Duration.ofMillis(batchRedeliverMs))
        }
    }

    private fun isRetryable(e: Exception): Boolean =
        e is org.apache.kafka.common.errors.RetriableException

    private fun getLastOffsetsByPartition(batch: List<ConsumerRecord<*, *>>) =
        batch.groupBy { TopicPartition(it.topic(), it.partition()) }
            .mapValues { (_, recs) -> recs.maxByOrNull { it.offset() }?.offset() }
}
