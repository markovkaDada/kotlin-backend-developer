package ru.otus.otuskotlin.yan.swiftorder.biz.repo

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderIdRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseErr
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseOk
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState

fun ICorChainDsl<Context>.repoRead() = worker("Читаем заказ из репозитория") {
    if (state == ContextState.FINISHING) {
        when (val result = repo.readOrder(DbOrderIdRequest(orderValidating.id))) {
            is DbOrderResponseOk  -> orderRepoDone = result.data
            is DbOrderResponseErr -> { errors.addAll(result.errors); state = ContextState.FAILING }
        }
    }
}
