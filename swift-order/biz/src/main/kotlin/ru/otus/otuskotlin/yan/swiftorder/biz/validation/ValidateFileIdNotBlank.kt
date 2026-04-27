package ru.otus.otuskotlin.yan.swiftorder.biz.validation

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.LogLevel
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.SOError

fun ICorChainDsl<Context>.validateFileIdNotBlank(title: String = "Проверка fileId") = worker(title) {
    if (state == ContextState.RUNNING && orderValidating.fileId.asString.isBlank()) {
        errors.add(SOError(code = "validation-file-id-empty", field = "fileId", message = "fileId must not be blank", level = LogLevel.ERROR))
        state = ContextState.FAILING
    }
}
