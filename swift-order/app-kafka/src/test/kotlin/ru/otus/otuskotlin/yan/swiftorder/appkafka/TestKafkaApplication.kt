package ru.otus.otuskotlin.yan.swiftorder.appkafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class TestKafkaApplication {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule()
}
