package ru.otus.otuskotlin.yan.swiftorder.api.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.IRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.IResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderResponseObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderStatusDto
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.ResponseResult
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class OrderSerializationTest {

    private val mapper = ObjectMapper().registerKotlinModule()

    @Test
    fun `OrderStatusDto all values serialize and deserialize correctly`() {
        OrderStatusDto.entries.forEach { status ->
            val json = mapper.writeValueAsString(status)
            val restored = mapper.readValue(json, OrderStatusDto::class.java)
            assertEquals(status, restored)
        }
    }

    @Test
    fun `OrderCreateRequest serializes and deserializes correctly`() {
        val request = OrderCreateRequest(
            requestType = "create",
            order = OrderCreateObject(
                description = "Cut steel 5mm",
                amount = BigDecimal("1500.00"),
                ownerId = "owner-1",
                fileId = "detail.dxf",
            ),
        )

        val json = mapper.writeValueAsString(request)
        val restored = mapper.readValue(json, OrderCreateRequest::class.java)

        assertEquals(request, restored)
    }

    @Test
    fun `IRequest deserialized as OrderCreateRequest via discriminator`() {
        val json = """{"requestType":"create","order":{"description":"Cut steel 5mm","amount":1500.00,"ownerId":"owner-1","fileId":"detail.dxf"}}"""

        val request = mapper.readValue(json, IRequest::class.java)

        assertIs<OrderCreateRequest>(request)
        assertEquals("Cut steel 5mm", request.order?.description)
        assertEquals(BigDecimal("1500.00"), request.order?.amount)
    }

    @Test
    fun `OrderCreateResponse serializes and deserializes correctly`() {
        val response = OrderCreateResponse(
            responseType = "create",
            result = ResponseResult.SUCCESS,
            order = OrderResponseObject(
                id = "order-1",
                description = "Cut steel 5mm",
                amount = BigDecimal("1500.00"),
                status = OrderStatusDto.NEW,
                ownerId = "owner-1",
                fileId = "detail.dxf",
            ),
        )

        val json = mapper.writeValueAsString(response)
        val restored = mapper.readValue(json, OrderCreateResponse::class.java)

        assertEquals(response, restored)
    }

    @Test
    fun `IResponse deserialized as OrderCreateResponse via discriminator`() {
        val json = """{"responseType":"create","result":"success","order":{"id":"order-1","description":"Cut steel 5mm","amount":1500.0,"status":"NEW","ownerId":"owner-1","fileId":"detail.dxf"}}"""

        val response = mapper.readValue(json, IResponse::class.java)

        assertIs<OrderCreateResponse>(response)
        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals("order-1", response.order?.id)
    }
}
