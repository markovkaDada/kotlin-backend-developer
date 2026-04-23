package ru.otus.otuskotlin.yan.swiftorder.models

import java.math.BigDecimal

data class SwiftOrder(
    val id: SwiftOrderId = SwiftOrderId(""),
    val description: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val status: SwiftOrderStatus = SwiftOrderStatus.NEW,
    val ownerId: SwiftOwnerId = SwiftOwnerId(""),
    val fileId: SwiftFileId = SwiftFileId(""),
)
