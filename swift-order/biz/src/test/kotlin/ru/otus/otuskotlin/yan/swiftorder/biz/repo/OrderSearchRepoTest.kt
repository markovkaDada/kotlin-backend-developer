package ru.otus.otuskotlin.yan.swiftorder.biz.repo

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.yan.swiftorder.biz.SwiftOrderProcessor
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import ru.otus.otuskotlin.yan.swiftorder.repoinmemory.SwiftOrderRepoInMemory
import ru.otus.otuskotlin.yan.swiftorder.repotests.BaseInitOrders
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderSearchRepoTest {

    private val repo = SwiftOrderRepoInMemory(initOrders = BaseInitOrders.all)
    private val processor = SwiftOrderProcessor(repo = repo)

    @Test
    fun `searchOrders by ownerId returns matching orders`() = runBlocking {
        val ctx = Context(
            command = Command.SEARCH,
            workMode = WorkMode.PROD,
            orderFilterRequest = DbOrderFilterRequest(ownerIdFilter = SwiftOwnerId("owner-1")),
        )
        processor.exec(ctx)

        assertTrue(ctx.errors.isEmpty(), "Expected no errors but got: ${ctx.errors}")
        assertEquals(ContextState.FINISHING, ctx.state)
        assertEquals(2, ctx.ordersRepoDone.size)
        assertTrue(ctx.ordersRepoDone.all { it.ownerId.asString == "owner-1" })
    }

    @Test
    fun `searchOrders by status returns matching orders`() = runBlocking {
        val ctx = Context(
            command = Command.SEARCH,
            workMode = WorkMode.PROD,
            orderFilterRequest = DbOrderFilterRequest(statusFilter = SwiftOrderStatus.NEW),
        )
        processor.exec(ctx)

        assertTrue(ctx.errors.isEmpty(), "Expected no errors but got: ${ctx.errors}")
        assertEquals(ContextState.FINISHING, ctx.state)
        assertTrue(ctx.ordersRepoDone.isNotEmpty())
        assertTrue(ctx.ordersRepoDone.all { it.status == SwiftOrderStatus.NEW })
    }
}
