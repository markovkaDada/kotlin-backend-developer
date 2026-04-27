package ru.otus.otuskotlin.yan.swiftorder.appkafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import ru.otus.otuskotlin.yan.swiftorder.appcommon.AppSettings
import ru.otus.otuskotlin.yan.swiftorder.biz.SwiftOrderProcessor

@SpringBootApplication
class TestKafkaApplication {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule()

    @Bean
    fun appSettings(): AppSettings = AppSettings(processor = SwiftOrderProcessor())
}
