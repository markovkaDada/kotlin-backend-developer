package ru.otus.otuskotlin.yan.swiftorder.biz.validation

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import java.math.BigDecimal

fun ICorChainDsl<Context>.validateAmountPositive(title: String = "Проверка суммы") = worker(title) {
    if (state == ContextState.RUNNING && orderValidating.amount <= BigDecimal.ZERO) {
        errors.add(SOError(code = "validation-amount-not-positive", field = "amount", message = "amount must be positive", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
