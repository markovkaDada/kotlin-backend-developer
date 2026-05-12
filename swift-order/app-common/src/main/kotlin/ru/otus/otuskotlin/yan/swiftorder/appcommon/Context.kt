package ru.otus.otuskotlin.yan.swiftorder.appcommon

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.yan.swiftorder.appcommon.error.SOError
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode

data class Context(
    var command: Command = Command.NONE,
    var state: ContextState = ContextState.NONE,

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: StubCase = StubCase.NONE,
    var orderRequest: SwiftOrder = SwiftOrder(),
    var orderValidating: SwiftOrder = SwiftOrder(),
    var orderFilterRequest: DbOrderFilterRequest = DbOrderFilterRequest(),
    var orderRepoDone: SwiftOrder = SwiftOrder(),
    var ordersRepoDone: MutableList<SwiftOrder> = mutableListOf(),
    var errors: MutableList<SOError> = mutableListOf(),

    var repo: ISwiftOrderRepo = ISwiftOrderRepo.NONE,

    var timeStart: Instant = Instant.fromEpochMilliseconds(Long.MIN_VALUE),
)
