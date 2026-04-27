package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase

fun ICorChainDsl<Context>.stubDbError(title: String = "Имитация ошибки работы с БД") = worker(title) {
    if (stubCase == StubCase.DB_ERROR) {
        errors.add(SOError(code = "db-error", group = "database", message = "database error occurred", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
