package ru.otus.otuskotlin.yan.swiftorder.appcommon.stubs

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import java.math.BigDecimal

object StubSearchSuccess {
    private val STUB_ORDERS = mutableListOf(
        SwiftOrder(
            id = SwiftOrderId("stub-order-1"),
            description = "Stub laser cutting order 1",
            amount = BigDecimal("100.00"),
            status = SwiftOrderStatus.NEW,
            ownerId = SwiftOwnerId("stub-owner-1"),
            fileId = SwiftFileId("stub-file-1"),
        ),
        SwiftOrder(
            id = SwiftOrderId("stub-order-2"),
            description = "Stub laser cutting order 2",
            amount = BigDecimal("200.00"),
            status = SwiftOrderStatus.CONFIRMED,
            ownerId = SwiftOwnerId("stub-owner-2"),
            fileId = SwiftFileId("stub-file-2"),
        ),
    )

    fun exec(ctx: Context) {
        ctx.ordersResponse = STUB_ORDERS
    }
}
