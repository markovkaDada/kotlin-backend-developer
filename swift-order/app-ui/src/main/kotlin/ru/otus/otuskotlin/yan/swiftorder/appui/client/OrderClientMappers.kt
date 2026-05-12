package ru.otus.otuskotlin.yan.swiftorder.appui.client

import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderResponseObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchFilter
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.yan.swiftorder.mappers.toInternal
import ru.otus.otuskotlin.yan.swiftorder.mappers.toTransport
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId

internal fun toSearchRequest(ownerId: String, status: SwiftOrderStatus?) = OrderSearchRequest(
    orderFilter = OrderSearchFilter(
        ownerId = ownerId.ifBlank { null },
        status = status?.toTransport(),
    )
)

internal fun SwiftOrder.toCreateRequest() = OrderCreateRequest(
    order = OrderCreateObject(
        description = description,
        amount = amount,
        ownerId = ownerId.asString,
        fileId = fileId.asString,
    )
)

internal fun SwiftOrder.toUpdateRequest() = OrderUpdateRequest(
    order = OrderUpdateObject(
        id = id.asString,
        description = description,
        amount = amount,
        status = status.toTransport(),
        ownerId = ownerId.asString,
        fileId = fileId.asString,
    )
)

internal fun OrderResponseObject.toSwiftOrder() = SwiftOrder(
    id = SwiftOrderId(id),
    description = description,
    amount = amount,
    status = status.toInternal(),
    ownerId = SwiftOwnerId(ownerId),
    fileId = SwiftFileId(fileId),
)
