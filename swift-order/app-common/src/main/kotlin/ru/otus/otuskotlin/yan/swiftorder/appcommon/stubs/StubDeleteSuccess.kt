package ru.otus.otuskotlin.yan.swiftorder.appcommon.stubs

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import java.math.BigDecimal

object StubDeleteSuccess {
    private val STUB_ORDER = SwiftOrder(
        id = SwiftOrderId("stub-order-1"),
        description = "Stub laser cutting order",
        amount = BigDecimal("100.00"),
        status = SwiftOrderStatus.CANCELLED,
        ownerId = SwiftOwnerId("stub-owner-1"),
        fileId = SwiftFileId("stub-file-1"),
    )

    fun exec(ctx: Context) {
        ctx.orderResponse = STUB_ORDER
    }
}
