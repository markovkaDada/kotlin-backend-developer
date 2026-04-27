package ru.otus.otuskotlin.yan.swiftorder.appui.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.EmbeddedKafkaZKBroker
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.*
import ru.otus.otuskotlin.yan.swiftorder.models.*
import java.math.BigDecimal
import java.time.Duration
import java.util.*
import kotlin.test.*

class KafkaOrderClientTest {

    private val objectMapper = ObjectMapper().registerKotlinModule()
    private lateinit var broker: EmbeddedKafkaZKBroker
    private lateinit var client: KafkaOrderClient

    @BeforeTest
    fun setup() {
        broker = EmbeddedKafkaZKBroker(
            1, false, 1,
            "swift-order-create-request", "swift-order-create-response",
            "swift-order-read-request",   "swift-order-read-response",
            "swift-order-delete-request", "swift-order-delete-response",
            "swift-order-search-request", "swift-order-search-response",
        ).also { it.afterPropertiesSet() }

        client = KafkaOrderClient(
            bootstrapServers = broker.brokersAsString,
            objectMapper = objectMapper,
        )
    }

    @AfterTest
    fun teardown() {
        client.close()
        broker.destroy()
    }

    @Test
    fun `create sends request to Kafka and receives response by correlationId`() = runBlocking {
        val expectedOrder = OrderResponseObject(
            id = "created-id", description = "Laser cut",
            amount = BigDecimal("750"), status = OrderStatusDto.NEW,
            ownerId = "owner-1", fileId = "part.dxf",
        )

        fakeBackend(
            requestTopic  = "swift-order-create-request",
            responseTopic = "swift-order-create-response",
            responseJson  = objectMapper.writeValueAsString(
                OrderCreateResponse(result = ResponseResult.SUCCESS, errors = null, order = expectedOrder)
            ),
        )

        val result = client.create(
            SwiftOrder(description = "Laser cut", amount = BigDecimal("750"),
                ownerId = SwiftOwnerId("owner-1"), fileId = SwiftFileId("part.dxf"))
        )

        assertEquals("created-id", result.id.asString)
        assertEquals("Laser cut", result.description)
        assertEquals(SwiftOrderStatus.NEW, result.status)
    }

    @Test
    fun `search returns mapped orders from Kafka response`() = runBlocking {
        val orders = listOf(
            OrderResponseObject(id = "s-1", description = "Order 1", amount = BigDecimal("100"),
                status = OrderStatusDto.CONFIRMED, ownerId = "o", fileId = "f"),
        )

        fakeBackend(
            requestTopic  = "swift-order-search-request",
            responseTopic = "swift-order-search-response",
            responseJson  = objectMapper.writeValueAsString(
                OrderSearchResponse(result = ResponseResult.SUCCESS, errors = null, orders = orders)
            ),
        )

        val result = client.search()

        assertEquals(1, result.size)
        assertEquals("s-1", result[0].id.asString)
        assertEquals(SwiftOrderStatus.CONFIRMED, result[0].status)
    }

    @Test
    fun `delete sends request and succeeds on SUCCESS response`() = runBlocking {
        fakeBackend(
            requestTopic  = "swift-order-delete-request",
            responseTopic = "swift-order-delete-response",
            responseJson  = objectMapper.writeValueAsString(
                OrderDeleteResponse(result = ResponseResult.SUCCESS, errors = null, order = null)
            ),
        )

        client.delete("order-to-delete")
    }

    @Test
    fun `create throws OrderClientException on ERROR response`() = runBlocking {
        fakeBackend(
            requestTopic  = "swift-order-create-request",
            responseTopic = "swift-order-create-response",
            responseJson  = objectMapper.writeValueAsString(
                OrderCreateResponse(
                    result = ResponseResult.ERROR,
                    errors = listOf(Error(message = "validation failed")),
                    order = null,
                )
            ),
        )

        assertFailsWith<OrderClientException> {
            client.create(SwiftOrder())
        }
    }

    // Starts a one-shot fake backend: reads one record from requestTopic, replies to responseTopic with correlationId header preserved.
    private fun fakeBackend(requestTopic: String, responseTopic: String, responseJson: String) {
        val props = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker.brokersAsString)
            put(ConsumerConfig.GROUP_ID_CONFIG, "test-backend-${UUID.randomUUID()}")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        }
        val producerProps = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker.brokersAsString)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        }

        Thread {
            KafkaConsumer<String, String>(props).use { consumer ->
                consumer.subscribe(listOf(requestTopic))
                KafkaProducer<String, String>(producerProps).use { producer ->
                    var done = false
                    while (!done) {
                        consumer.poll(Duration.ofMillis(200)).forEach { record ->
                            val correlationId = record.headers()
                                .lastHeader("correlationId")?.value()?.let { String(it) }
                            val reply = ProducerRecord<String, String>(responseTopic, responseJson)
                            if (correlationId != null)
                                reply.headers().add("correlationId", correlationId.toByteArray())
                            producer.send(reply).get()
                            done = true
                        }
                    }
                }
            }
        }.also { it.isDaemon = true; it.start() }
    }
}
