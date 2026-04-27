package ru.otus.otuskotlin.yan.swiftorder.biz.repo

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState

fun ICorChainDsl<Context>.repoRead() = worker("Читаем заказ из репозитория") {
    if (state == ContextState.FINISHING) {
        println("REPO: reading order by id ${orderValidating.id}")
        orderRepoDone = orderValidating.copy()
    }
}
