package ru.otus.otuskotlin.yan.swiftorder.biz.validation

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

class OrderCreateValidationTest {
    private val processor = SwiftOrderProcessor()

    private fun validCtx() = Context(
        command = Command.CREATE,
        workMode = WorkMode.PROD,
        stubCase = StubCase.NONE,
        orderRequest = SwiftOrder(
            id = SwiftOrderId(""),
            description = "Valid description",
            amount = BigDecimal("100.00"),
            ownerId = SwiftOwnerId("owner-1"),
            fileId = SwiftFileId("file-1"),
        ),
    )

    @Test
    fun `valid CREATE request has no errors`() = runBlocking {
        val ctx = validCtx()
        processor.exec(ctx)
        assertTrue(ctx.errors.isEmpty(), "Expected no errors but got: ${ctx.errors}")
        assertEquals(ContextState.FINISHING, ctx.state)
    }

    @Test
    fun `blank description causes validation error`() = runBlocking {
        val ctx = validCtx().copy(orderRequest = validCtx().orderRequest.copy(description = "   "))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-description-empty" })
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `zero amount causes validation error`() = runBlocking {
        val ctx = validCtx().copy(orderRequest = validCtx().orderRequest.copy(amount = BigDecimal.ZERO))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-amount-not-positive" })
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `negative amount causes validation error`() = runBlocking {
        val ctx = validCtx().copy(orderRequest = validCtx().orderRequest.copy(amount = BigDecimal("-1")))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-amount-not-positive" })
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `blank ownerId causes validation error`() = runBlocking {
        val ctx = validCtx().copy(orderRequest = validCtx().orderRequest.copy(ownerId = SwiftOwnerId("  ")))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-owner-id-empty" })
        assertEquals(ContextState.FAILING, ctx.state)
    }

    @Test
    fun `blank fileId causes validation error`() = runBlocking {
        val ctx = validCtx().copy(orderRequest = validCtx().orderRequest.copy(fileId = SwiftFileId("")))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-file-id-empty" })
        assertEquals(ContextState.FAILING, ctx.state)
    }
}
