package ru.otus.otuskotlin.yan.swiftorder.biz.validation

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError

fun ICorChainDsl<Context>.validateOwnerIdNotBlank(title: String = "Проверка ownerId") = worker(title) {
    if (state == ContextState.RUNNING && orderValidating.ownerId.asString.isBlank()) {
        errors.add(SOError(code = "validation-owner-id-empty", field = "ownerId", message = "ownerId must not be blank", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
