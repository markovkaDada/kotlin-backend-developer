package ru.otus.otuskotlin.yan.swiftorder.appkafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class OrderKafkaProducerTest {

    @Test
    fun `send creates ProducerRecord with correct topic and message`() {
        val kafkaProducer = mock<KafkaProducer<String, String>>()
        val producer = OrderKafkaProducer(kafkaProducer)
        val captor = argumentCaptor<ProducerRecord<String, String>>()

        producer.send("response-topic", "response-body")

        verify(kafkaProducer).send(captor.capture())
        verify(kafkaProducer).flush()
        assertEquals("response-topic", captor.firstValue.topic())
        assertEquals("response-body", captor.firstValue.value())
        assertNull(captor.firstValue.key())
    }
}
