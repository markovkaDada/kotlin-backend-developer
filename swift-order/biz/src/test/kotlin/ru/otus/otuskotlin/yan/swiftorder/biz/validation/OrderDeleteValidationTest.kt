package ru.otus.otuskotlin.yan.swiftorder.biz.validation

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.biz.SwiftOrderProcessor
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import ru.otus.otuskotlin.yan.swiftorder.repoinmemory.SwiftOrderRepoInMemory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderDeleteValidationTest {
    private val processor = SwiftOrderProcessor(
        repo = SwiftOrderRepoInMemory(initOrders = listOf(SwiftOrder(id = SwiftOrderId("some-id"))))
    )

    @Test
    fun `valid DELETE request has no errors`() = runBlocking {
        val ctx = Context(
            command = Command.DELETE,
            workMode = WorkMode.PROD,
            stubCase = StubCase.NONE,
            orderRequest = SwiftOrder(id = SwiftOrderId("some-id")),
        )
        processor.exec(ctx)
        assertTrue(ctx.errors.isEmpty(), "Expected no errors but got: ${ctx.errors}")
        assertEquals(ContextState.FINISHING, ctx.state)
    }

    @Test
    fun `blank id causes validation error`() = runBlocking {
        val ctx = Context(
            command = Command.DELETE,
            workMode = WorkMode.PROD,
            stubCase = StubCase.NONE,
            orderRequest = SwiftOrder(id = SwiftOrderId("")),
        )
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-id-empty" })
        assertEquals(ContextState.FAILING, ctx.state)
    }
}
