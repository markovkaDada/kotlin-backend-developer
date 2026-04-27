package ru.otus.otuskotlin.yan.swiftorder.mappers

import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.Error
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.IRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.IResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDebug
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.ResponseResult
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode

// ---- Request → Context ----

fun Context.fromTransport(request: IRequest) = when (request) {
    is OrderCreateRequest -> fromTransport(request)
    is OrderReadRequest   -> fromTransport(request)
    is OrderUpdateRequest -> fromTransport(request)
    is OrderDeleteRequest -> fromTransport(request)
    is OrderSearchRequest -> fromTransport(request)
    else -> throw IllegalArgumentException("Unknown request type: ${request::class.simpleName}")
}

fun Context.fromTransport(request: OrderCreateRequest) {
    command = Command.CREATE
    workMode = request.debug.toWorkMode()
    stubCase = request.debug.toStubCase()
    orderRequest = request.toInternal()
}

fun Context.fromTransport(request: OrderReadRequest) {
    command = Command.READ
    workMode = request.debug.toWorkMode()
    stubCase = request.debug.toStubCase()
    orderRequest = SwiftOrder(id = SwiftOrderId(request.order?.id ?: ""))
}

fun Context.fromTransport(request: OrderUpdateRequest) {
    command = Command.UPDATE
    workMode = request.debug.toWorkMode()
    stubCase = request.debug.toStubCase()
    orderRequest = request.toInternal()
}

fun Context.fromTransport(request: OrderDeleteRequest) {
    command = Command.DELETE
    workMode = request.debug.toWorkMode()
    stubCase = request.debug.toStubCase()
    orderRequest = SwiftOrder(id = SwiftOrderId(request.order?.id ?: ""))
}

fun Context.fromTransport(request: OrderSearchRequest) {
    command = Command.SEARCH
    workMode = request.debug.toWorkMode()
    stubCase = request.debug.toStubCase()
}

// ---- Context → Response ----

fun Context.toTransport(): IResponse = when (command) {
    Command.CREATE -> toTransportCreate()
    Command.READ   -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.SEARCH -> toTransportSearch()
    Command.NONE   -> throw IllegalStateException("Command is NONE, cannot determine response type")
}

fun Context.toTransportCreate(): OrderCreateResponse = OrderCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order  = orderRepoDone.toResponseObject().takeIf { orderRepoDone.id.asString.isNotBlank() },
)

fun Context.toTransportRead(): OrderReadResponse = OrderReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order  = orderRepoDone.toResponseObject().takeIf { orderRepoDone.id.asString.isNotBlank() },
)

fun Context.toTransportUpdate(): OrderUpdateResponse = OrderUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order  = orderRepoDone.toResponseObject().takeIf { orderRepoDone.id.asString.isNotBlank() },
)

fun Context.toTransportDelete(): OrderDeleteResponse = OrderDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order  = orderRepoDone.toResponseObject().takeIf { orderRepoDone.id.asString.isNotBlank() },
)

fun Context.toTransportSearch(): OrderSearchResponse = OrderSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    orders = ordersRepoDone.map { it.toResponseObject() }.takeIf { it.isNotEmpty() },
)

// ---- State / Error helpers ----

fun ContextState.toResult(): ResponseResult? = when (this) {
    ContextState.RUNNING   -> ResponseResult.SUCCESS
    ContextState.FINISHING -> ResponseResult.SUCCESS
    ContextState.FAILING   -> ResponseResult.ERROR
    ContextState.NONE      -> null
}

fun List<SOError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportError() }
    .takeIf { it.isNotEmpty() }

fun SOError.toTransportError() = Error(
    code    = code.takeIf { it.isNotBlank() },
    group   = group.takeIf { it.isNotBlank() },
    field   = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

// ---- Debug helpers ----

fun OrderDebug?.toWorkMode(): WorkMode = when (this?.mode) {
    OrderRequestDebugMode.STUB -> WorkMode.STUB
    else -> WorkMode.PROD
}

fun OrderDebug?.toStubCase(): StubCase = when (this?.stub) {
    OrderRequestDebugStubs.SUCCESS -> StubCase.SUCCESS
    else -> StubCase.NONE
}
