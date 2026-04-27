package ru.otus.otuskotlin.yan.swiftorder.appui.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.yan.swiftorder.appui.client.OrderClient
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import kotlin.test.Test
import kotlin.test.assertEquals

class TransportSwitchTest {

    private val httpClient = stubClient("http")
    private val kafkaClient = stubClient("kafka")

    @Test
    fun `transport=kafka selects kafka client`() = testApplication {
        application { routing { get("/t") { call.respondText(call.orderClient(httpClient, kafkaClient).name()) } } }
        assertEquals("kafka", client.get("/t?transport=kafka").bodyAsText())
    }

    @Test
    fun `transport=http selects http client`() = testApplication {
        application { routing { get("/t") { call.respondText(call.orderClient(httpClient, kafkaClient).name()) } } }
        assertEquals("http", client.get("/t?transport=http").bodyAsText())
    }

    @Test
    fun `missing transport defaults to http`() = testApplication {
        application { routing { get("/t") { call.respondText(call.orderClient(httpClient, kafkaClient).name()) } } }
        assertEquals("http", client.get("/t").bodyAsText())
    }

    private fun stubClient(name: String) = object : OrderClient {
        fun name() = name
        override suspend fun search() = emptyList<SwiftOrder>()
        override suspend fun read(id: String) = SwiftOrder()
        override suspend fun create(order: SwiftOrder) = SwiftOrder()
        override suspend fun update(order: SwiftOrder) = SwiftOrder()
        override suspend fun delete(id: String) {}
    }

    private fun OrderClient.name() =
        if (this === kafkaClient) "kafka" else "http"
}
