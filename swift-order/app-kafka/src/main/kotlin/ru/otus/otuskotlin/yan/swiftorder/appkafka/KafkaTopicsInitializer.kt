package ru.otus.otuskotlin.yan.swiftorder.appkafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaAdmin
import ru.otus.otuskotlin.yan.swiftorder.appkafka.config.SwiftOrderTopicsProperties

class KafkaTopicsInitializer(
    private val props: SwiftOrderTopicsProperties,
    private val kafkaAdmin: KafkaAdmin,
) {
    @EventListener(ApplicationReadyEvent::class)
    fun createTopics() {
        val topics = props.getAllTopics()
            .map { NewTopic(it.name, it.partitions, it.replicationFactor) }
            .toTypedArray()
        kafkaAdmin.createOrModifyTopics(*topics)
    }
}
