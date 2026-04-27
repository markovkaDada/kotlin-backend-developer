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

fun ICorChainDsl<Context>.stubUpdateSuccess(title: String = "Имитация успешного обновления") = worker(title) {
    if (stubCase == StubCase.SUCCESS) {
        orderRepoDone = SwiftOrder(
            id = SwiftOrderId("stub-order-1"),
            description = "Stub laser cutting order",
            amount = BigDecimal("100.00"),
            status = SwiftOrderStatus.NEW,
            ownerId = SwiftOwnerId("stub-owner-1"),
            fileId = SwiftFileId("stub-file-1"),
        )
        state = ContextState.FINISHING
    }
}
