package ru.otus.otuskotlin.yan.swiftorder.repotests

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderIdRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseErr
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseOk
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoOrderDeleteTest {
    abstract val repo: ISwiftOrderRepo

    @Test
    fun `deleteOrder removes order and returns it`() = runBlocking {
        val result = repo.deleteOrder(DbOrderIdRequest(BaseInitOrders.order2.id))
        val ok = assertIs<DbOrderResponseOk>(result)
        assertEquals(BaseInitOrders.order2, ok.data)
    }

    @Test
    fun `deleteOrder returns error for non-existent id`(): Unit = runBlocking {
        val result = repo.deleteOrder(DbOrderIdRequest(SwiftOrderId("ghost-id")))
        assertIs<DbOrderResponseErr>(result)
    }

    @Test
    fun `deleted order is no longer readable`(): Unit = runBlocking {
        repo.deleteOrder(DbOrderIdRequest(BaseInitOrders.order3.id))
        val result = repo.readOrder(DbOrderIdRequest(BaseInitOrders.order3.id))
        assertIs<DbOrderResponseErr>(result)
    }
}
