package ru.otus.otuskotlin.yan.swiftorder.appkafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class OrderKafkaProducer(
    private val producer: KafkaProducer<String, String>,
) {
    fun send(topic: String, message: String) {
        producer.send(ProducerRecord(topic, message))
        producer.flush()
    }
}
