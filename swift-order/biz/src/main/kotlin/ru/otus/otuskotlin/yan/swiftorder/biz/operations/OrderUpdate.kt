package ru.otus.otuskotlin.yan.swiftorder.biz.operations

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.biz.general.copyRequestToValidating
import ru.otus.otuskotlin.yan.swiftorder.biz.general.finishValidation
import ru.otus.otuskotlin.yan.swiftorder.biz.general.operation
import ru.otus.otuskotlin.yan.swiftorder.biz.general.stubs
import ru.otus.otuskotlin.yan.swiftorder.biz.general.trimDescription
import ru.otus.otuskotlin.yan.swiftorder.biz.general.trimId
import ru.otus.otuskotlin.yan.swiftorder.biz.repo.repoUpdate
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubDbError
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubNoCase
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubUpdateSuccess
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubValidationBadAmount
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubValidationBadDescription
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubValidationBadFileId
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubValidationBadId
import ru.otus.otuskotlin.yan.swiftorder.biz.stubs.stubValidationBadOwnerId
import ru.otus.otuskotlin.yan.swiftorder.biz.validation.validateAmountPositive
import ru.otus.otuskotlin.yan.swiftorder.biz.validation.validateDescriptionNotBlank
import ru.otus.otuskotlin.yan.swiftorder.biz.validation.validateFileIdNotBlank
import ru.otus.otuskotlin.yan.swiftorder.biz.validation.validateIdNotBlank
import ru.otus.otuskotlin.yan.swiftorder.biz.validation.validateOwnerIdNotBlank
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.chain
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode

fun ICorChainDsl<Context>.orderUpdate() = operation(Command.UPDATE) {
    stubs {
        stubUpdateSuccess()
        stubValidationBadId()
        stubValidationBadDescription()
        stubValidationBadAmount()
        stubValidationBadOwnerId()
        stubValidationBadFileId()
        stubDbError()
        stubNoCase()
    }
    chain {
        on { workMode != WorkMode.STUB && state == ContextState.RUNNING }
        copyRequestToValidating()
        trimId()
        trimDescription()
        validateIdNotBlank()
        validateDescriptionNotBlank()
        validateAmountPositive()
        validateOwnerIdNotBlank()
        validateFileIdNotBlank()
        finishValidation()
        repoUpdate()
    }
}
