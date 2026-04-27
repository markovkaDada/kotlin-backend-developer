package ru.otus.otuskotlin.yan.swiftorder.mappers

import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderResponseObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderStatusDto
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import java.math.BigDecimal
import java.util.UUID

// ---- Transport → Internal ----

fun OrderCreateRequest.toInternal(): SwiftOrder = SwiftOrder(
    id = SwiftOrderId(UUID.randomUUID().toString()),
    description = order?.description ?: "",
    amount = order?.amount ?: BigDecimal.ZERO,
    status = SwiftOrderStatus.NEW,
    ownerId = SwiftOwnerId(order?.ownerId ?: ""),
    fileId = SwiftFileId(order?.fileId ?: ""),
)

fun OrderUpdateRequest.toInternal(): SwiftOrder = SwiftOrder(
    id = SwiftOrderId(order?.id ?: ""),
    description = order?.description ?: "",
    amount = order?.amount ?: BigDecimal.ZERO,
    status = order?.status?.toInternal() ?: SwiftOrderStatus.NEW,
    ownerId = SwiftOwnerId(order?.ownerId ?: ""),
    fileId = SwiftFileId(order?.fileId ?: ""),
)

fun OrderStatusDto.toInternal(): SwiftOrderStatus = when (this) {
    OrderStatusDto.NEW -> SwiftOrderStatus.NEW
    OrderStatusDto.CONFIRMED -> SwiftOrderStatus.CONFIRMED
    OrderStatusDto.IN_PROGRESS -> SwiftOrderStatus.IN_PROGRESS
    OrderStatusDto.COMPLETED -> SwiftOrderStatus.COMPLETED
    OrderStatusDto.CANCELLED -> SwiftOrderStatus.CANCELLED
}

// ---- Internal → Transport ----

fun SwiftOrderStatus.toTransport(): OrderStatusDto = when (this) {
    SwiftOrderStatus.NEW -> OrderStatusDto.NEW
    SwiftOrderStatus.CONFIRMED -> OrderStatusDto.CONFIRMED
    SwiftOrderStatus.IN_PROGRESS -> OrderStatusDto.IN_PROGRESS
    SwiftOrderStatus.COMPLETED -> OrderStatusDto.COMPLETED
    SwiftOrderStatus.CANCELLED -> OrderStatusDto.CANCELLED
}

fun SwiftOrder.toResponseObject(): OrderResponseObject = OrderResponseObject(
    id = id.asString,
    description = description,
    amount = amount,
    status = status.toTransport(),
    ownerId = ownerId.asString,
    fileId = fileId.asString,
)
