package ru.otus.otuskotlin.yan.swiftorder.appcommon

import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProcessorStubTest {

    private val processor = Processor()

    private fun stubContext(command: Command) = Context(
        command = command,
        workMode = WorkMode.STUB,
        stubCase = StubCase.SUCCESS,
    )

    @Test
    fun `CREATE stub returns stub order`() {
        val ctx = stubContext(Command.CREATE)
        processor.exec(ctx)
        assertEquals("stub-order-1", ctx.orderResponse.id.asString)
        assertEquals("Stub laser cutting order", ctx.orderResponse.description)
        assertTrue(ctx.errors.isEmpty())
    }

    @Test
    fun `READ stub returns stub order`() {
        val ctx = stubContext(Command.READ)
        processor.exec(ctx)
        assertEquals("stub-order-1", ctx.orderResponse.id.asString)
        assertTrue(ctx.errors.isEmpty())
    }

    @Test
    fun `UPDATE stub returns stub order`() {
        val ctx = stubContext(Command.UPDATE)
        processor.exec(ctx)
        assertEquals("stub-order-1", ctx.orderResponse.id.asString)
        assertTrue(ctx.errors.isEmpty())
    }

    @Test
    fun `DELETE stub returns stub order`() {
        val ctx = stubContext(Command.DELETE)
        processor.exec(ctx)
        assertEquals("stub-order-1", ctx.orderResponse.id.asString)
        assertTrue(ctx.errors.isEmpty())
    }

    @Test
    fun `SEARCH stub returns list of stub orders`() {
        val ctx = stubContext(Command.SEARCH)
        processor.exec(ctx)
        assertEquals(2, ctx.ordersResponse.size)
        assertEquals("stub-order-1", ctx.ordersResponse[0].id.asString)
        assertEquals("stub-order-2", ctx.ordersResponse[1].id.asString)
        assertTrue(ctx.errors.isEmpty())
    }

    @Test
    fun `PROD mode adds not-implemented error`() {
        val ctx = Context(command = Command.CREATE, workMode = WorkMode.PROD)
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals("not-implemented", ctx.errors[0].code)
    }
}
