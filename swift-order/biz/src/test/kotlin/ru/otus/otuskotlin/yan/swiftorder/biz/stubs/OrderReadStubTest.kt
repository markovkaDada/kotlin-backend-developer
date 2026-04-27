package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.biz.SwiftOrderProcessor
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderReadStubTest {
    private val processor = SwiftOrderProcessor()

    private fun stubCtx(stubCase: StubCase) = Context(
        command = Command.READ,
        workMode = WorkMode.STUB,
        stubCase = stubCase,
        orderRequest = SwiftOrder(id = SwiftOrderId("test-id")),
    )

    @Test
    fun `READ SUCCESS returns stub order`() = runBlocking {
        val ctx = stubCtx(StubCase.SUCCESS)
        processor.exec(ctx)
        assertEquals("stub-order-1", ctx.orderRepoDone.id.asString)
        assertTrue(ctx.errors.isEmpty())
        assertEquals(ContextState.FINISHING, ctx.state)
    }

    @Test
    fun `READ VALIDATION_BAD_ID returns error`() = runBlocking {
        val ctx = stubCtx(StubCase.VALIDATION_BAD_ID)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("validation-id-empty", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `READ DB_ERROR returns db error`() = runBlocking {
        val ctx = stubCtx(StubCase.DB_ERROR)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("db-error", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `READ NO_CASE returns stub-not-found error`() = runBlocking {
        val ctx = stubCtx(StubCase.NO_CASE)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("stub-not-found", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }
}
