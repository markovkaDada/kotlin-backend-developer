package ru.otus.otuskotlin.yan.swiftorder.biz.repo

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.biz.SwiftOrderProcessor
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import ru.otus.otuskotlin.yan.swiftorder.repoinmemory.SwiftOrderRepoInMemory
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class OrderCreateRepoTest {

    private val repo = SwiftOrderRepoInMemory()
    private val processor = SwiftOrderProcessor(repo = repo)

    @Test
    fun `createOrder via processor stores order with generated id`() = runBlocking {
        val ctx = Context(
            command = Command.CREATE,
            workMode = WorkMode.PROD,
            orderRequest = SwiftOrder(
                description = "Laser cut sheet",
                amount = BigDecimal("200.00"),
                status = SwiftOrderStatus.NEW,
                ownerId = SwiftOwnerId("owner-biz"),
                fileId = SwiftFileId("file-biz"),
            ),
        )
        processor.exec(ctx)

        assertTrue(ctx.errors.isEmpty(), "Expected no errors but got: ${ctx.errors}")
        assertEquals(ContextState.FINISHING, ctx.state)
        assertNotEquals("", ctx.orderRepoDone.id.asString)
        assertEquals("Laser cut sheet", ctx.orderRepoDone.description)
        assertEquals(BigDecimal("200.00"), ctx.orderRepoDone.amount)
    }

    @Test
    fun `createOrder with blank description returns validation error`() = runBlocking {
        val ctx = Context(
            command = Command.CREATE,
            workMode = WorkMode.PROD,
            orderRequest = SwiftOrder(
                description = "  ",
                amount = BigDecimal("100.00"),
                ownerId = SwiftOwnerId("owner-biz"),
                fileId = SwiftFileId("file-biz"),
            ),
        )
        processor.exec(ctx)

        assertTrue(ctx.errors.isNotEmpty())
        assertEquals(ContextState.FAILING, ctx.state)
    }
}
