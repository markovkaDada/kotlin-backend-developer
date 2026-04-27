package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError

fun ICorChainDsl<Context>.stubNoCase(title: String = "Ошибка: запрошенный стаб недопустим") = worker(title) {
    // fires only when state is still RUNNING — meaning no previous stub matched
    if (state == ContextState.RUNNING) {
        errors.add(SOError(code = "stub-not-found", message = "stub case '$stubCase' is not supported", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
