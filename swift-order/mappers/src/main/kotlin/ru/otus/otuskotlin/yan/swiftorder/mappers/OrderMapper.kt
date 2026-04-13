package ru.otus.otuskotlin.yan.swiftorder.mappers

import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderResponseObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderStatusDto
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateResponse
import ru.otus.otuskotlin.yan.swiftorder.common.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.common.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.common.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.common.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.common.models.SwiftOwnerId
import java.util.UUID

// ---- Transport → Internal ----

fun OrderCreateRequest.toInternal(): SwiftOrder {
    val o = requireNotNull(order) { "order must not be null" }
    require(o.description.isNotBlank()) { "description must not be blank" }
    require(o.ownerId.isNotBlank()) { "ownerId must not be blank" }
    require(o.fileId.isNotBlank()) { "fileId must not be blank" }
    return SwiftOrder(
        id = SwiftOrderId(UUID.randomUUID().toString()),
        description = o.description,
        amount = o.amount,
        status = SwiftOrderStatus.NEW,
        ownerId = SwiftOwnerId(o.ownerId),
        fileId = SwiftFileId(o.fileId),
    )
}

fun OrderUpdateRequest.toInternal(): SwiftOrder {
    val o = requireNotNull(order) { "order must not be null" }
    require(o.id.isNotBlank()) { "id must not be blank" }
    require(o.description.isNotBlank()) { "description must not be blank" }
    require(o.ownerId.isNotBlank()) { "ownerId must not be blank" }
    require(o.fileId.isNotBlank()) { "fileId must not be blank" }
    return SwiftOrder(
        id = SwiftOrderId(o.id),
        description = o.description,
        amount = o.amount,
        status = o.status.toInternal(),
        ownerId = SwiftOwnerId(o.ownerId),
        fileId = SwiftFileId(o.fileId),
    )
}

fun OrderStatusDto.toInternal(): SwiftOrderStatus = when (this) {
    OrderStatusDto.NEW -> SwiftOrderStatus.NEW
    OrderStatusDto.CONFIRMED -> SwiftOrderStatus.CONFIRMED
    OrderStatusDto.IN_PROGRESS -> SwiftOrderStatus.IN_PROGRESS
    OrderStatusDto.COMPLETED -> SwiftOrderStatus.COMPLETED
    OrderStatusDto.CANCELLED -> SwiftOrderStatus.CANCELLED
}

// ---- Internal → Transport ----

fun SwiftOrder.toTransportCreate(): OrderCreateResponse = OrderCreateResponse(order = toResponseObject())

fun SwiftOrder.toTransportRead(): OrderReadResponse = OrderReadResponse(order = toResponseObject())

fun SwiftOrder.toTransportUpdate(): OrderUpdateResponse = OrderUpdateResponse(order = toResponseObject())

fun SwiftOrder.toTransportDelete(): OrderDeleteResponse = OrderDeleteResponse(order = toResponseObject())

fun SwiftOrderStatus.toTransport(): OrderStatusDto = when (this) {
    SwiftOrderStatus.NEW -> OrderStatusDto.NEW
    SwiftOrderStatus.CONFIRMED -> OrderStatusDto.CONFIRMED
    SwiftOrderStatus.IN_PROGRESS -> OrderStatusDto.IN_PROGRESS
    SwiftOrderStatus.COMPLETED -> OrderStatusDto.COMPLETED
    SwiftOrderStatus.CANCELLED -> OrderStatusDto.CANCELLED
}

private fun SwiftOrder.toResponseObject(): OrderResponseObject = OrderResponseObject(
    id = id.asString,
    description = description,
    amount = amount,
    status = status.toTransport(),
    ownerId = ownerId.asString,
    fileId = fileId.asString,
)
