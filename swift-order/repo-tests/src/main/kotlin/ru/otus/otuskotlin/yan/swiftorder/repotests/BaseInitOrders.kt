package ru.otus.otuskotlin.yan.swiftorder.repotests

import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import java.math.BigDecimal

object BaseInitOrders {
    val order1 = SwiftOrder(
        id = SwiftOrderId("order-1"),
        description = "First order",
        amount = BigDecimal("100.00"),
        status = SwiftOrderStatus.NEW,
        ownerId = SwiftOwnerId("owner-1"),
        fileId = SwiftFileId("file-1"),
    )
    val order2 = SwiftOrder(
        id = SwiftOrderId("order-2"),
        description = "Second order",
        amount = BigDecimal("200.00"),
        status = SwiftOrderStatus.CONFIRMED,
        ownerId = SwiftOwnerId("owner-1"),
        fileId = SwiftFileId("file-2"),
    )
    val order3 = SwiftOrder(
        id = SwiftOrderId("order-3"),
        description = "Third order",
        amount = BigDecimal("300.00"),
        status = SwiftOrderStatus.NEW,
        ownerId = SwiftOwnerId("owner-2"),
        fileId = SwiftFileId("file-3"),
    )

    val all = listOf(order1, order2, order3)
}
