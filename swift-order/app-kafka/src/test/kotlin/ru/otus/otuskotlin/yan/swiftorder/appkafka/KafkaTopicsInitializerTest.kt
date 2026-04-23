package ru.otus.otuskotlin.yan.swiftorder.appkafka

import org.apache.kafka.clients.admin.NewTopic
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.kafka.core.KafkaAdmin
import ru.otus.otuskotlin.yan.swiftorder.appkafka.config.SwiftOrderTopicsProperties

class KafkaTopicsInitializerTest {

    @Test
    fun `createTopics creates all topics with correct names`() {
        val props = SwiftOrderTopicsProperties()
        val kafkaAdmin = mock<KafkaAdmin>()

        KafkaTopicsInitializer(props, kafkaAdmin).createTopics()

        val expectedTopics = props.getAllTopics()
            .map { NewTopic(it.name, it.partitions, it.replicationFactor) }
            .toTypedArray()
        verify(kafkaAdmin).createOrModifyTopics(*expectedTopics)
    }
}
