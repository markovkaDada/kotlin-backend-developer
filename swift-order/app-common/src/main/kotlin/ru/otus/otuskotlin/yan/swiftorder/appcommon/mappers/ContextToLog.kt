package ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.yan.swiftorder.apilog.CommonLogModel
import ru.otus.otuskotlin.yan.swiftorder.apilog.ErrorLogModel
import ru.otus.otuskotlin.yan.swiftorder.apilog.OrderLog
import ru.otus.otuskotlin.yan.swiftorder.apilog.SwiftOrderLogModel
import ru.otus.otuskotlin.yan.swiftorder.apilog.SwiftOrderLogOperation
import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.appcommon.error.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder

fun Context.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "swift-order",
    order = toOrderLog(),
    errors = errors.map { it.toLog() },
)

private fun Context.toOrderLog(): SwiftOrderLogModel? {
    val orderNone = SwiftOrder()
    return SwiftOrderLogModel(
        operation = command.toLog(),
        requestOrder = orderRequest.takeIf { it != orderNone }?.toLog(),
        responseOrder = orderResponse.takeIf { it != orderNone }?.toLog(),
        responseOrders = ordersResponse.takeIf { it.isNotEmpty() }?.map { it.toLog() },
    ).takeIf { it != SwiftOrderLogModel() }
}

private fun Command.toLog(): SwiftOrderLogOperation? = when (this) {
    Command.CREATE -> SwiftOrderLogOperation.CREATE
    Command.READ   -> SwiftOrderLogOperation.READ
    Command.UPDATE -> SwiftOrderLogOperation.UPDATE
    Command.DELETE -> SwiftOrderLogOperation.DELETE
    Command.SEARCH -> SwiftOrderLogOperation.SEARCH
    Command.NONE   -> null
}

private fun SwiftOrder.toLog() = OrderLog(
    id = id.asString.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    amount = amount.takeIf { it.signum() != 0 }?.toPlainString(),
    status = status.name,
    ownerId = ownerId.asString.takeIf { it.isNotBlank() },
    fileId = fileId.asString.takeIf { it.isNotBlank() },
)

private fun SOError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)
