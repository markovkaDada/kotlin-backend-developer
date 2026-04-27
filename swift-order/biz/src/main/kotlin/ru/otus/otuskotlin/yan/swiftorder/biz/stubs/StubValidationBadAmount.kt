package ru.otus.otuskotlin.yan.swiftorder.biz.stubs

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.StubCase

fun ICorChainDsl<Context>.stubValidationBadAmount(title: String = "Имитация ошибки валидации суммы") = worker(title) {
    if (stubCase == StubCase.VALIDATION_BAD_AMOUNT) {
        errors.add(SOError(code = "validation-amount-not-positive", field = "amount", message = "amount must be positive", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
