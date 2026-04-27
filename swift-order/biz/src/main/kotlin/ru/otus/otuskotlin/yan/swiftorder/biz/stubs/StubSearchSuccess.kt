package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import java.math.BigDecimal

fun ICorChainDsl<Context>.stubSearchSuccess(title: String = "Имитация успешного поиска") = worker(title) {
    if (stubCase == StubCase.SUCCESS) {
        ordersRepoDone = mutableListOf(
            SwiftOrder(
                id = SwiftOrderId("stub-order-1"),
                description = "Stub laser cutting order",
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
        state = ContextState.FINISHING
    }
}
