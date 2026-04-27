package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase

fun ICorChainDsl<Context>.stubValidationBadDescription(title: String = "Имитация ошибки валидации описания") = worker(title) {
    if (stubCase == StubCase.VALIDATION_BAD_DESCRIPTION) {
        errors.add(SOError(code = "validation-description-empty", field = "description", message = "description must not be blank", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
