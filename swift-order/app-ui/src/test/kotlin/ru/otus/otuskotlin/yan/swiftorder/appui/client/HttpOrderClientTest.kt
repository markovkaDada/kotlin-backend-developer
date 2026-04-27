package ru.otus.otuskotlin.yan.swiftorder.appui.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.*
import ru.otus.otuskotlin.yan.swiftorder.models.*
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HttpOrderClientTest {

    private val objectMapper = ObjectMapper().registerKotlinModule()

    private fun clientWith(handler: MockRequestHandler): HttpOrderClient {
        val engine = MockEngine(handler)
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) { jackson() }
        }
        return HttpOrderClient("http://localhost", httpClient)
    }

    private fun jsonHeaders() =
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    @Test
    fun `search sends POST to search endpoint and maps orders`() = runBlocking {
        val client = clientWith { request ->
            assertEquals("/v1/order/search", request.url.encodedPath)
            assertEquals("POST", request.method.value)
            respond(
                objectMapper.writeValueAsString(
                    OrderSearchResponse(
                        result = ResponseResult.SUCCESS,
                        errors = null,
                        orders = listOf(
                            OrderResponseObject(
                                id = "abc-123", description = "Cut steel",
                                amount = BigDecimal("500"), status = OrderStatusDto.NEW,
                                ownerId = "owner-1", fileId = "file.dxf",
                            )
                        ),
                    )
                ),
                HttpStatusCode.OK, jsonHeaders(),
            )
        }

        val result = client.search()

        assertEquals(1, result.size)
        assertEquals("abc-123", result[0].id.asString)
        assertEquals("Cut steel", result[0].description)
        assertEquals(BigDecimal("500"), result[0].amount)
        assertEquals(SwiftOrderStatus.NEW, result[0].status)
        assertEquals("owner-1", result[0].ownerId.asString)
    }

    @Test
    fun `search throws OrderClientException on error result`() = runBlocking {
        val client = clientWith { _ ->
            respond(
                objectMapper.writeValueAsString(
                    OrderSearchResponse(
                        result = ResponseResult.ERROR,
                        errors = listOf(Error(message = "backend error")),
                        orders = null,
                    )
                ),
                HttpStatusCode.OK, jsonHeaders(),
            )
        }

        assertFailsWith<OrderClientException> { client.search() }
    }

    @Test
    fun `read sends POST to read endpoint with id`() = runBlocking {
        val client = clientWith { request ->
            assertEquals("/v1/order/read", request.url.encodedPath)
            respond(
                objectMapper.writeValueAsString(
                    OrderReadResponse(
                        result = ResponseResult.SUCCESS, errors = null,
                        order = OrderResponseObject(
                            id = "order-1", description = "Desc",
                            amount = BigDecimal("100"), status = OrderStatusDto.CONFIRMED,
                            ownerId = "o1", fileId = "f1",
                        ),
                    )
                ),
                HttpStatusCode.OK, jsonHeaders(),
            )
        }

        val result = client.read("order-1")

        assertEquals("order-1", result.id.asString)
        assertEquals(SwiftOrderStatus.CONFIRMED, result.status)
    }

    @Test
    fun `create sends POST to create endpoint and returns created order`() = runBlocking {
        val client = clientWith { request ->
            assertEquals("/v1/order/create", request.url.encodedPath)
            respond(
                objectMapper.writeValueAsString(
                    OrderCreateResponse(
                        result = ResponseResult.SUCCESS, errors = null,
                        order = OrderResponseObject(
                            id = "new-id", description = "New",
                            amount = BigDecimal("200"), status = OrderStatusDto.NEW,
                            ownerId = "owner", fileId = "file",
                        ),
                    )
                ),
                HttpStatusCode.OK, jsonHeaders(),
            )
        }

        val order = SwiftOrder(
            description = "New", amount = BigDecimal("200"),
            ownerId = SwiftOwnerId("owner"), fileId = SwiftFileId("file"),
        )
        val result = client.create(order)

        assertEquals("new-id", result.id.asString)
    }

    @Test
    fun `delete sends POST to delete endpoint`() = runBlocking {
        var calledPath = ""
        val client = clientWith { request ->
            calledPath = request.url.encodedPath
            respond(
                objectMapper.writeValueAsString(
                    OrderDeleteResponse(result = ResponseResult.SUCCESS, errors = null, order = null)
                ),
                HttpStatusCode.OK, jsonHeaders(),
            )
        }

        client.delete("order-1")

        assertEquals("/v1/order/delete", calledPath)
    }
}
