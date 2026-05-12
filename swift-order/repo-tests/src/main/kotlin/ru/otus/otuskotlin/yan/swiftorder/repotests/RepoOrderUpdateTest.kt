package ru.otus.otuskotlin.yan.swiftorder.repotests

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseErr
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseOk
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoOrderUpdateTest {
    abstract val repo: ISwiftOrderRepo

    @Test
    fun `updateOrder returns updated order`() = runBlocking {
        val updated = BaseInitOrders.order1.copy(
            description = "Updated description",
            status = SwiftOrderStatus.CONFIRMED,
        )
        val result = repo.updateOrder(DbOrderRequest(updated))
        val ok = assertIs<DbOrderResponseOk>(result)
        assertEquals("Updated description", ok.data.description)
        assertEquals(SwiftOrderStatus.CONFIRMED, ok.data.status)
        assertEquals(BaseInitOrders.order1.id, ok.data.id)
    }

    @Test
    fun `updateOrder returns error for non-existent id`(): Unit = runBlocking {
        val order = BaseInitOrders.order1.copy(id = SwiftOrderId("ghost-id"))
        val result = repo.updateOrder(DbOrderRequest(order))
        assertIs<DbOrderResponseErr>(result)
    }
}
