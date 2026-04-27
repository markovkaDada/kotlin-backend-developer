package ru.otus.otuskotlin.yan.swiftorder.cor.handlers

import ru.otus.otuskotlin.yan.swiftorder.cor.CorDslMarker
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorExec
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorExecDsl

abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String = "",
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockExcept: suspend T.(Throwable) -> Unit = { e -> throw e },
) : ICorExec<T> {

    protected abstract suspend fun handle(context: T)

    override suspend fun exec(context: T) {
        if (blockOn(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                blockExcept(context, e)
            }
        }
    }
}

@CorDslMarker
abstract class CorExecDsl<T> : ICorExecDsl<T> {
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockExcept: suspend T.(e: Throwable) -> Unit = { e -> throw e }

    override var title: String = ""
    override var description: String = ""

    override fun on(function: suspend T.() -> Boolean) { blockOn = function }
    override fun except(function: suspend T.(e: Throwable) -> Unit) { blockExcept = function }
}
