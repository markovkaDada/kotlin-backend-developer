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

abstract class RepoOrderReadTest {
    abstract val repo: ISwiftOrderRepo

    @Test
    fun `readOrder returns existing order by id`() = runBlocking {
        val result = repo.readOrder(DbOrderIdRequest(BaseInitOrders.order1.id))
        val ok = assertIs<DbOrderResponseOk>(result)
        assertEquals(BaseInitOrders.order1, ok.data)
    }

    @Test
    fun `readOrder returns error for unknown id`(): Unit = runBlocking {
        val result = repo.readOrder(DbOrderIdRequest(SwiftOrderId("non-existent-id")))
        assertIs<DbOrderResponseErr>(result)
    }

    @Test
    fun `readOrder returns error for blank id`(): Unit = runBlocking {
        val result = repo.readOrder(DbOrderIdRequest(SwiftOrderId("")))
        assertIs<DbOrderResponseErr>(result)
    }
}
