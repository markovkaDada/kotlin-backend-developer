package ru.otus.otuskotlin.yan.swiftorder.biz.validation

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError

fun ICorChainDsl<Context>.validateIdNotBlank(title: String = "Проверка id") = worker(title) {
    if (state == ContextState.RUNNING && orderValidating.id.asString.isBlank()) {
        errors.add(SOError(code = "validation-id-empty", field = "id", message = "id must not be blank", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
