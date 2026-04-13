package ru.otus.otuskotlin.yan.swiftorder.common.models

import java.math.BigDecimal

data class SwiftOrder(
    val id: SwiftOrderId,
    val description: String,
    val amount: BigDecimal,
    val status: SwiftOrderStatus,
    val ownerId: SwiftOwnerId,
    val fileId: SwiftFileId,
)
