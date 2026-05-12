package ru.otus.otuskotlin.yan.swiftorder.repotests

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderIdRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseOk
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class RepoOrderCreateTest {
    abstract val repo: ISwiftOrderRepo

    private val createRequest = DbOrderRequest(
        order = SwiftOrder(
            description = "New laser order",
            amount = BigDecimal("150.00"),
            status = SwiftOrderStatus.NEW,
            ownerId = SwiftOwnerId("owner-99"),
            fileId = SwiftFileId("file-99"),
        )
    )

    @Test
    fun `createOrder returns order with generated non-empty id`() = runBlocking {
        val result = repo.createOrder(createRequest)
        val ok = assertIs<DbOrderResponseOk>(result)
        assertNotEquals("", ok.data.id.asString)
        assertEquals("New laser order", ok.data.description)
        assertEquals(BigDecimal("150.00"), ok.data.amount)
    }

    @Test
    fun `createOrder stores order retrievable by readOrder`() = runBlocking {
        val created = assertIs<DbOrderResponseOk>(repo.createOrder(createRequest))
        val read = assertIs<DbOrderResponseOk>(
            repo.readOrder(DbOrderIdRequest(created.data.id))
        )
        assertEquals(created.data, read.data)
    }
}
