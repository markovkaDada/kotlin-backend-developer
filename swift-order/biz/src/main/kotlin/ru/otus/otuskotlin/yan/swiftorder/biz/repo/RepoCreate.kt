package ru.otus.otuskotlin.yan.swiftorder.biz.repo

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseErr
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseOk
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState

fun ICorChainDsl<Context>.repoCreate() = worker("Создаём заказ в репозитории") {
    if (state == ContextState.FINISHING) {
        when (val result = repo.createOrder(DbOrderRequest(orderValidating))) {
            is DbOrderResponseOk  -> orderRepoDone = result.data
            is DbOrderResponseErr -> { errors.addAll(result.errors); state = ContextState.FAILING }
        }
    }
}
