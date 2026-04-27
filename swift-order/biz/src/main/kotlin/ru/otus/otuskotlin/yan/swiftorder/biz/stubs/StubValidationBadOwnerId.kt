package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase

fun ICorChainDsl<Context>.stubValidationBadOwnerId(title: String = "Имитация ошибки валидации ownerId") = worker(title) {
    if (stubCase == StubCase.VALIDATION_BAD_OWNER_ID) {
        errors.add(SOError(code = "validation-owner-id-empty", field = "ownerId", message = "ownerId must not be blank", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
