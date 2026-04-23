package ru.otus.otuskotlin.yan.swiftorder.appspring.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.yan.swiftorder.appcommon.AppSettings

@Configuration
class AppConfig {

    @Bean
    fun appSettings(): AppSettings {
        return AppSettings()
    }

    @Bean
    fun  objectMapper(): ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        .enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
}