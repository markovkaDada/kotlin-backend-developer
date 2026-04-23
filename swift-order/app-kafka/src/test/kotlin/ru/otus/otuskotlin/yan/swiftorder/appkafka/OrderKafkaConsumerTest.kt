package ru.otus.otuskotlin.yan.swiftorder.appkafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.NetworkException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.support.Acknowledgment
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
import ru.otus.otuskotlin.yan.swiftorder.appcommon.AppSettings
import ru.otus.otuskotlin.yan.swiftorder.appkafka.config.SwiftOrderTopicsProperties
import java.math.BigDecimal
import java.time.Duration

class OrderKafkaConsumerTest {

    private val objectMapper = jacksonObjectMapper()
    private val props = SwiftOrderTopicsProperties()
    private val producer = mock<OrderKafkaProducer>()
    private val consumer = OrderKafkaConsumer(
        containerFactory = mock<ConcurrentKafkaListenerContainerFactory<String, String>>(),
        props = props,
        appSettings = AppSettings(),
        objectMapper = objectMapper,
        producer = producer,
    )
    private val debug = OrderDebug(mode = OrderRequestDebugMode.STUB, stub = OrderRequestDebugStubs.SUCCESS)

    // ---- helpers ----

    private fun records(topicName: String, vararg values: String?): ConsumerRecords<String?, String?> {
        val list = values.mapIndexed { i, v -> ConsumerRecord(topicName, 0, i.toLong(), "key$i", v) }
        return ConsumerRecords(mapOf(TopicPartition(topicName, 0) to list))
    }

    // ---- handleCreate ----

    @Test
    fun `handleCreate acknowledges and sends response on success`() {
        val ack = mock<Acknowledgment>()
        val json = objectMapper.writeValueAsString(
            OrderCreateRequest(debug = debug, order = OrderCreateObject("desc", BigDecimal.ONE, "o1", "f1"))
        )

        consumer.handleCreate(records(props.createRequestTopic.name, json), ack)

        verify(ack).acknowledge()
        verify(producer).send(eq(props.createResponseTopic.name), any())
    }

    @Test
    fun `handleCreate skips record and still acknowledges on non-retryable error`() {
        val ack = mock<Acknowledgment>()

        consumer.handleCreate(records(props.createRequestTopic.name, "not-valid-json"), ack)

        verify(ack).acknowledge()
        verify(producer, never()).send(any(), any())
    }

    @Test
    fun `handleCreate nacks batch on retryable Kafka error`() {
        val ack = mock<Acknowledgment>()
        val json = objectMapper.writeValueAsString(
            OrderCreateRequest(debug = debug, order = OrderCreateObject("desc", BigDecimal.ONE, "o1", "f1"))
        )
        doThrow(NetworkException("broker unreachable")).whenever(producer).send(any(), any())

        consumer.handleCreate(records(props.createRequestTopic.name, json), ack)

        verify(ack).nack(eq(0), any<Duration>())
        verify(ack, never()).acknowledge()
    }

    @Test
    fun `handleCreate processes all records in batch and acknowledges once`() {
        val ack = mock<Acknowledgment>()
        val json = objectMapper.writeValueAsString(
            OrderCreateRequest(debug = debug, order = OrderCreateObject("desc", BigDecimal.ONE, "o1", "f1"))
        )

        consumer.handleCreate(records(props.createRequestTopic.name, json, json, json), ack)

        verify(ack).acknowledge()
        verify(producer, times(3)).send(eq(props.createResponseTopic.name), any())
    }

    @Test
    fun `handleCreate skips invalid record in mixed batch and processes valid ones`() {
        val ack = mock<Acknowledgment>()
        val validJson = objectMapper.writeValueAsString(
            OrderCreateRequest(debug = debug, order = OrderCreateObject("desc", BigDecimal.ONE, "o1", "f1"))
        )

        consumer.handleCreate(records(props.createRequestTopic.name, validJson, "bad-json", validJson), ack)

        verify(ack).acknowledge()
        // only 2 valid records produce a response, bad-json is skipped
        verify(producer, times(2)).send(eq(props.createResponseTopic.name), any())
    }

    // ---- handleRead ----

    @Test
    fun `handleRead acknowledges and sends response on success`() {
        val ack = mock<Acknowledgment>()
        val json = objectMapper.writeValueAsString(
            OrderReadRequest(debug = debug, order = OrderReadObject(id = "stub-order-1"))
        )

        consumer.handleRead(records(props.readRequestTopic.name, json), ack)

        verify(ack).acknowledge()
        verify(producer).send(eq(props.readResponseTopic.name), any())
    }

    // ---- handleUpdate ----

    @Test
    fun `handleUpdate acknowledges and sends response on success`() {
        val ack = mock<Acknowledgment>()
        val json = objectMapper.writeValueAsString(
            OrderUpdateRequest(
                debug = debug,
                order = OrderUpdateObject(
                    id = "stub-order-1", description = "upd", amount = BigDecimal.ONE,
                    status = OrderStatusDto.CONFIRMED, ownerId = "o1", fileId = "f1",
                ),
            )
        )

        consumer.handleUpdate(records(props.updateRequestTopic.name, json), ack)

        verify(ack).acknowledge()
        verify(producer).send(eq(props.updateResponseTopic.name), any())
    }

    // ---- handleDelete ----

    @Test
    fun `handleDelete acknowledges and sends response on success`() {
        val ack = mock<Acknowledgment>()
        val json = objectMapper.writeValueAsString(
            OrderDeleteRequest(debug = debug, order = OrderDeleteObject(id = "stub-order-1"))
        )

        consumer.handleDelete(records(props.deleteRequestTopic.name, json), ack)

        verify(ack).acknowledge()
        verify(producer).send(eq(props.deleteResponseTopic.name), any())
    }

    // ---- handleSearch ----

    @Test
    fun `handleSearch acknowledges and sends response on success`() {
        val ack = mock<Acknowledgment>()
        val json = objectMapper.writeValueAsString(OrderSearchRequest(debug = debug))

        consumer.handleSearch(records(props.searchRequestTopic.name, json), ack)

        verify(ack).acknowledge()
        verify(producer).send(eq(props.searchResponseTopic.name), any())
    }
}
