package ru.otus.otuskotlin.yan.swiftorder.appcommon

import ru.otus.otuskotlin.yan.swiftorder.appcommon.error.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode
import kotlinx.datetime.Instant

data class Context(
    var command: Command = Command.NONE,
    var state: ContextState = ContextState.NONE,

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: StubCase = StubCase.NONE,
    var orderRequest: SwiftOrder = SwiftOrder(),
    var orderValidating: SwiftOrder = SwiftOrder(),
    var orderRepoDone: SwiftOrder = SwiftOrder(),       // результат, полученный из репозитория
    var ordersRepoDone: MutableList<SwiftOrder> = mutableListOf(), // список, полученный из репозитория
    var errors: MutableList<SOError> = mutableListOf(),

    var timeStart: Instant = Instant.fromEpochMilliseconds(Long.MIN_VALUE),
)
