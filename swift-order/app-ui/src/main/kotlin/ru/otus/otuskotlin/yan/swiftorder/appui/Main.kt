package ru.otus.otuskotlin.yan.swiftorder.appui

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.otus.otuskotlin.yan.swiftorder.appui.client.HttpOrderClient
import ru.otus.otuskotlin.yan.swiftorder.appui.client.KafkaOrderClient
import ru.otus.otuskotlin.yan.swiftorder.appui.routes.orderRoutes

fun main() {
    val port           = System.getenv("UI_PORT")?.toIntOrNull() ?: 8081
    val backendUrl     = System.getenv("BACKEND_URL") ?: "http://localhost:8080"
    val kafkaBootstrap = System.getenv("KAFKA_BOOTSTRAP") ?: "localhost:9092"

    val objectMapper = ObjectMapper().registerKotlinModule()

    val httpClient = HttpOrderClient(
        baseUrl = backendUrl,
        httpClient = HttpClient(CIO) {
            install(ContentNegotiation) { jackson() }
        },
    )

    val kafkaClient = KafkaOrderClient(
        bootstrapServers = kafkaBootstrap,
        objectMapper = objectMapper,
    )

    embeddedServer(Netty, port = port) {
        orderRoutes(httpClient, kafkaClient)
    }.start(wait = true)
}
