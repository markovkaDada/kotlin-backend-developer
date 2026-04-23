package ru.otus.otuskotlin.yan.swiftorder.appkafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDebug
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderStatusDto
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.ResponseResult
import java.math.BigDecimal
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
    partitions = 1,
    topics = [
        "swift-order-create-request", "swift-order-create-response",
        "swift-order-read-request", "swift-order-read-response",
        "swift-order-update-request", "swift-order-update-response",
        "swift-order-delete-request", "swift-order-delete-response",
        "swift-order-search-request", "swift-order-search-response",
    ]
)
@TestPropertySource(properties = ["spring.kafka.bootstrap-servers=\${spring.embedded.kafka.brokers}"])
class KafkaConsumerStubTest {

    @Autowired
    private lateinit var embeddedKafka: EmbeddedKafkaBroker

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private fun makeTemplate(): KafkaTemplate<String, String> {
        val producerProps = KafkaTestUtils.producerProps(embeddedKafka)
        producerProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        producerProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return KafkaTemplate(DefaultKafkaProducerFactory<String, String>(producerProps))
    }

    private fun makeConsumer(topic: String): LinkedBlockingQueue<String> {
        val consumerProps = KafkaTestUtils.consumerProps("test-consumer-$topic", "true", embeddedKafka)
        consumerProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        consumerProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        consumerProps[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        val records = LinkedBlockingQueue<String>()
        val containerProps = ContainerProperties(topic)
        containerProps.messageListener = MessageListener<String, String> { msg -> records.add(msg.value()) }
        val container = KafkaMessageListenerContainer(DefaultKafkaConsumerFactory<String, String>(consumerProps), containerProps)
        container.start()
        return records
    }

    private val debug = OrderDebug(mode = OrderRequestDebugMode.STUB, stub = OrderRequestDebugStubs.SUCCESS)

    @Test
    fun testCreate() {
        val records = makeConsumer("swift-order-create-response")
        val template = makeTemplate()
        val request = OrderCreateRequest(
            debug = debug,
            order = OrderCreateObject(description = "test", amount = BigDecimal.TEN, ownerId = "o1", fileId = "f1"),
        )
        template.send("swift-order-create-request", objectMapper.writeValueAsString(request))
        val raw = records.poll(10, TimeUnit.SECONDS)!!
        val node = objectMapper.readTree(raw)
        Assertions.assertEquals("stub-order-1", node["order"]["id"].asText())
        Assertions.assertEquals(ResponseResult.SUCCESS.value, node["result"].asText())
    }

    @Test
    fun testRead() {
        val records = makeConsumer("swift-order-read-response")
        val template = makeTemplate()
        val request = OrderReadRequest(debug = debug, order = OrderReadObject(id = "stub-order-1"))
        template.send("swift-order-read-request", objectMapper.writeValueAsString(request))
        val raw = records.poll(10, TimeUnit.SECONDS)!!
        val node = objectMapper.readTree(raw)
        Assertions.assertEquals("stub-order-1", node["order"]["id"].asText())
        Assertions.assertEquals(ResponseResult.SUCCESS.value, node["result"].asText())
    }

    @Test
    fun testUpdate() {
        val records = makeConsumer("swift-order-update-response")
        val template = makeTemplate()
        val request = OrderUpdateRequest(
            debug = debug,
            order = OrderUpdateObject(
                id = "stub-order-1", description = "upd", amount = BigDecimal.ONE,
                status = OrderStatusDto.CONFIRMED, ownerId = "o1", fileId = "f1"
            ),
        )
        template.send("swift-order-update-request", objectMapper.writeValueAsString(request))
        val raw = records.poll(10, TimeUnit.SECONDS)!!
        val node = objectMapper.readTree(raw)
        Assertions.assertEquals("stub-order-1", node["order"]["id"].asText())
        Assertions.assertEquals(ResponseResult.SUCCESS.value, node["result"].asText())
    }

    @Test
    fun testDelete() {
        val records = makeConsumer("swift-order-delete-response")
        val template = makeTemplate()
        val request = OrderDeleteRequest(debug = debug, order = OrderDeleteObject(id = "stub-order-1"))
        template.send("swift-order-delete-request", objectMapper.writeValueAsString(request))
        val raw = records.poll(10, TimeUnit.SECONDS)!!
        val node = objectMapper.readTree(raw)
        Assertions.assertEquals(ResponseResult.SUCCESS.value, node["result"].asText())
    }

    @Test
    fun testSearch() {
        val records = makeConsumer("swift-order-search-response")
        val template = makeTemplate()
        val request = OrderSearchRequest(debug = debug)
        template.send("swift-order-search-request", objectMapper.writeValueAsString(request))
        val raw = records.poll(10, TimeUnit.SECONDS)!!
        val node = objectMapper.readTree(raw)
        Assertions.assertEquals("stub-order-1", node["orders"][0]["id"].asText())
        Assertions.assertEquals(ResponseResult.SUCCESS.value, node["result"].asText())
    }
}