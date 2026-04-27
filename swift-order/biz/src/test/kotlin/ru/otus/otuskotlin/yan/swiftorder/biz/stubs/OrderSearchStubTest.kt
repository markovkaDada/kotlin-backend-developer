package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.biz.SwiftOrderProcessor
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderSearchStubTest {
    private val processor = SwiftOrderProcessor()

    private fun stubCtx(stubCase: StubCase) = Context(
        command = Command.SEARCH,
        workMode = WorkMode.STUB,
        stubCase = stubCase,
    )

    @Test
    fun `SEARCH SUCCESS returns 2 stub orders`() = runBlocking {
        val ctx = stubCtx(StubCase.SUCCESS)
        processor.exec(ctx)
        assertEquals(2, ctx.ordersRepoDone.size)
        assertEquals("stub-order-1", ctx.ordersRepoDone[0].id.asString)
        assertEquals("stub-order-2", ctx.ordersRepoDone[1].id.asString)
        assertTrue(ctx.errors.isEmpty())
        assertEquals(ContextState.FINISHING, ctx.state)
    }

    @Test
    fun `SEARCH DB_ERROR returns db error`() = runBlocking {
        val ctx = stubCtx(StubCase.DB_ERROR)
        processor.exec(ctx)
        assertEquals("db-error", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `SEARCH NO_CASE returns stub-not-found error`() = runBlocking {
        val ctx = stubCtx(StubCase.NO_CASE)
        processor.exec(ctx)
        assertEquals("stub-not-found", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }
}
