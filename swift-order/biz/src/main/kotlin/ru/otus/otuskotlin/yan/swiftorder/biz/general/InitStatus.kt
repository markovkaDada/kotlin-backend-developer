package ru.otus.otuskotlin.yan.swiftorder.biz.general

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.ContextState

fun ICorChainDsl<Context>.initStatus(title: String = "Инициализация статуса") = worker(title) {
    state = ContextState.RUNNING
    errors = mutableListOf()
}
