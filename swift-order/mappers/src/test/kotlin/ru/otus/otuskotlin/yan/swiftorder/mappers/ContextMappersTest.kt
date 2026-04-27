package ru.otus.otuskotlin.yan.swiftorder.mappers

import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.IRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDebug
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.ResponseResult
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ContextMappersTest {

    private val stubDebug = OrderDebug(
        mode = OrderRequestDebugMode.STUB,
        stub = OrderRequestDebugStubs.SUCCESS,
    )

    @Test
    fun `fromTransport OrderCreateRequest sets CREATE command with STUB mode`() {
        val ctx = Context()
        ctx.fromTransport(
            OrderCreateRequest(
                debug = stubDebug,
                order = OrderCreateObject(description = "desc", amount = BigDecimal.ONE, ownerId = "o1", fileId = "f1"),
            )
        )
        assertEquals(Command.CREATE, ctx.command)
        assertEquals(WorkMode.STUB, ctx.workMode)
        assertEquals(StubCase.SUCCESS, ctx.stubCase)
    }

    @Test
    fun `fromTransport OrderReadRequest sets READ command and order id`() {
        val ctx = Context()
        ctx.fromTransport(OrderReadRequest(order = OrderReadObject(id = "test-id")))
        assertEquals(Command.READ, ctx.command)
        assertEquals("test-id", ctx.orderRequest.id.asString)
    }

    @Test
    fun `fromTransport IRequest dispatch works for search`() {
        val ctx = Context()
        ctx.fromTransport(OrderSearchRequest() as IRequest)
        assertEquals(Command.SEARCH, ctx.command)
    }

    @Test
    fun `toTransportCreate maps RUNNING state to SUCCESS result`() {
        val ctx = Context(command = Command.CREATE, state = ContextState.RUNNING)
        val response = ctx.toTransportCreate()
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun `toTransportCreate maps FAILING state to ERROR result`() {
        val ctx = Context(command = Command.CREATE, state = ContextState.FAILING)
        val response = ctx.toTransportCreate()
        assertEquals(ResponseResult.ERROR, response.result)
    }

    @Test
    fun `toTransportSearch maps RUNNING state to SUCCESS with no orders`() {
        val ctx = Context(command = Command.SEARCH, state = ContextState.RUNNING)
        val response = ctx.toTransportSearch()
        assertEquals(ResponseResult.SUCCESS, response.result)
        assertNull(response.orders)
    }

    @Test
    fun `SOContextState NONE maps to null result`() {
        val ctx = Context(command = Command.CREATE, state = ContextState.NONE)
        val response = ctx.toTransportCreate()
        assertNull(response.result)
    }
}
