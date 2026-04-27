package ru.otus.otuskotlin.yan.swiftorder.biz.operations

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.biz.general.copyRequestToValidating
import ru.otus.otuskotlin.yan.swiftorder.biz.general.finishValidation
import ru.otus.otuskotlin.yan.swiftorder.biz.general.operation
import ru.otus.otuskotlin.yan.swiftorder.biz.general.stubs
import ru.otus.otuskotlin.yan.swiftorder.biz.general.trimId
import ru.otus.otuskotlin.yan.swiftorder.biz.repo.repoDelete
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubDbError
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubDeleteSuccess
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubNoCase
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubValidationBadId
import ru.otus.otuskotlin.yan.swiftorder.biz.validation.validateIdNotBlank
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.chain
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode

fun ICorChainDsl<Context>.orderDelete() = operation(Command.DELETE) {
    stubs {
        stubDeleteSuccess()
        stubValidationBadId()
        stubDbError()
        stubNoCase()
    }
    chain {
        on { workMode != WorkMode.STUB && state == ContextState.RUNNING }
        copyRequestToValidating()
        trimId()
        validateIdNotBlank()
        finishValidation()
        repoDelete()
    }
}
