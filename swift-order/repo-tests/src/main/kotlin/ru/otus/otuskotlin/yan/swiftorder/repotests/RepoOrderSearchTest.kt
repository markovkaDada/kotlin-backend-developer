package ru.otus.otuskotlin.yan.swiftorder.repotests

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

abstract class RepoOrderSearchTest {
    abstract val repo: ISwiftOrderRepo

    @Test
    fun `searchOrders by ownerId returns owner orders`() = runBlocking {
        val result = repo.searchOrders(
            DbOrderFilterRequest(ownerIdFilter = SwiftOwnerId("owner-1"))
        )
        val ok = assertIs<DbOrdersResponseOk>(result)
        assertEquals(2, ok.data.size)
        assertTrue(ok.data.all { it.ownerId.asString == "owner-1" })
    }

    @Test
    fun `searchOrders by status returns matching orders`() = runBlocking {
        val result = repo.searchOrders(
            DbOrderFilterRequest(statusFilter = SwiftOrderStatus.NEW)
        )
        val ok = assertIs<DbOrdersResponseOk>(result)
        assertTrue(ok.data.isNotEmpty())
        assertTrue(ok.data.all { it.status == SwiftOrderStatus.NEW })
    }

    @Test
    fun `searchOrders by ownerId and status returns intersection`() = runBlocking {
        val result = repo.searchOrders(
            DbOrderFilterRequest(
                ownerIdFilter = SwiftOwnerId("owner-1"),
                statusFilter = SwiftOrderStatus.CONFIRMED,
            )
        )
        val ok = assertIs<DbOrdersResponseOk>(result)
        assertEquals(1, ok.data.size)
        assertEquals(BaseInitOrders.order2.id, ok.data[0].id)
    }

    @Test
    fun `searchOrders with empty filter returns all orders`() = runBlocking {
        val result = repo.searchOrders(DbOrderFilterRequest())
        val ok = assertIs<DbOrdersResponseOk>(result)
        assertTrue(ok.data.size >= 3)
    }
}
