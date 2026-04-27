package ru.otus.otuskotlin.yan.swiftorder.appkafka.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.listener.ContainerProperties
import ru.otus.otuskotlin.yan.swiftorder.appcommon.AppSettings
import ru.otus.otuskotlin.yan.swiftorder.appkafka.KafkaTopicsInitializer
import ru.otus.otuskotlin.yan.swiftorder.appkafka.OrderKafkaConsumer
import ru.otus.otuskotlin.yan.swiftorder.appkafka.OrderKafkaProducer

const val ORDER_CONSUMER_CONTAINER_FACTORY = "orderConsumerContainerFactory"

@Configuration
class KafkaTopicsConfig {

    @Bean
    fun swiftOrderTopicsProperties() = SwiftOrderTopicsProperties()

    @Bean(ORDER_CONSUMER_CONTAINER_FACTORY)
    fun orderConsumerContainerFactory(
        kafkaProperties: KafkaProperties,
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        val consumerConfig = HashMap(kafkaProperties.buildConsumerProperties()).also {
            it[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        }
        val consumerFactory = DefaultKafkaConsumerFactory(
            consumerConfig,
            StringDeserializer(),
            StringDeserializer(),
        )
        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            this.consumerFactory = consumerFactory
            isBatchListener = true
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }
    }

    @Bean
    @ConditionalOnProperty(name = ["swift-order.create-topics"], havingValue = "true", matchIfMissing = false)
    fun kafkaTopicsInitializer(
        props: SwiftOrderTopicsProperties,
        kafkaAdmin: KafkaAdmin,
    ) = KafkaTopicsInitializer(props, kafkaAdmin)

    @Bean
    @ConditionalOnProperty(name = ["app.kafka.enabled"], havingValue = "true", matchIfMissing = true)
    fun orderKafkaConsumer(
        @Qualifier(ORDER_CONSUMER_CONTAINER_FACTORY)
        containerFactory: ConcurrentKafkaListenerContainerFactory<String, String>,
        props: SwiftOrderTopicsProperties,
        appSettings: AppSettings,
        objectMapper: ObjectMapper,
        producer: OrderKafkaProducer,
    ) = OrderKafkaConsumer(containerFactory, props, appSettings, objectMapper, producer)

    @Bean
    @ConditionalOnProperty(name = ["app.kafka.enabled"], havingValue = "true", matchIfMissing = true)
    fun orderKafkaProducer(kafkaProperties: KafkaProperties): OrderKafkaProducer {
        val producerConfig = HashMap(kafkaProperties.buildProducerProperties()).also {
            it[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            it[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        }
        return OrderKafkaProducer(KafkaProducer(producerConfig))
    }
}
