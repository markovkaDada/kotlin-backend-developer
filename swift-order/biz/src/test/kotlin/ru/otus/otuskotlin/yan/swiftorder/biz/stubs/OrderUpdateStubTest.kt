package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.biz.SwiftOrderProcessor
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderUpdateStubTest {
    private val processor = SwiftOrderProcessor()

    private fun stubCtx(stubCase: StubCase) = Context(
        command = Command.UPDATE,
        workMode = WorkMode.STUB,
        stubCase = stubCase,
        orderRequest = SwiftOrder(
            id = SwiftOrderId("test-id"),
            description = "Test order",
            amount = BigDecimal("50.00"),
            ownerId = SwiftOwnerId("test-owner"),
            fileId = SwiftFileId("test-file"),
        ),
    )

    @Test
    fun `UPDATE SUCCESS returns stub order`() = runBlocking {
        val ctx = stubCtx(StubCase.SUCCESS)
        processor.exec(ctx)
        assertEquals("stub-order-1", ctx.orderRepoDone.id.asString)
        assertTrue(ctx.errors.isEmpty())
        assertEquals(ContextState.FINISHING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_ID returns error`() = runBlocking {
        val ctx = stubCtx(StubCase.VALIDATION_BAD_ID)
        processor.exec(ctx)
        assertEquals("validation-id-empty", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_DESCRIPTION returns error`() = runBlocking {
        val ctx = stubCtx(StubCase.VALIDATION_BAD_DESCRIPTION)
        processor.exec(ctx)
        assertEquals("validation-description-empty", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_AMOUNT returns error`() = runBlocking {
        val ctx = stubCtx(StubCase.VALIDATION_BAD_AMOUNT)
        processor.exec(ctx)
        assertEquals("validation-amount-not-positive", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_OWNER_ID returns error`() = runBlocking {
        val ctx = stubCtx(StubCase.VALIDATION_BAD_OWNER_ID)
        processor.exec(ctx)
        assertEquals("validation-owner-id-empty", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_FILE_ID returns error`() = runBlocking {
        val ctx = stubCtx(StubCase.VALIDATION_BAD_FILE_ID)
        processor.exec(ctx)
        assertEquals("validation-file-id-empty", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE DB_ERROR returns db error`() = runBlocking {
        val ctx = stubCtx(StubCase.DB_ERROR)
        processor.exec(ctx)
        assertEquals("db-error", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE NO_CASE returns stub-not-found error`() = runBlocking {
        val ctx = stubCtx(StubCase.NO_CASE)
        processor.exec(ctx)
        assertEquals("stub-not-found", ctx.errors.first().code)
        assertEquals(ContextState.FAILING, ctx.state)
    }
}
