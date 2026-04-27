package ru.otus.otuskotlin.yan.swiftorder.biz.validation

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError

fun ICorChainDsl<Context>.validateDescriptionNotBlank(title: String = "Проверка описания") = worker(title) {
    if (state == ContextState.RUNNING && orderValidating.description.isBlank()) {
        errors.add(SOError(code = "validation-description-empty", field = "description", message = "description must not be blank", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
