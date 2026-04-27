package ru.otus.otuskotlin.yan.swiftorder.cor.handlers

import ru.otus.otuskotlin.yan.swiftorder.cor.CorDslMarker
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorExec
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorExecDsl

class CorChain<T>(
    private val execs: List<ICorExec<T>>,
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = { e -> throw e },
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun handle(context: T) {
        execs.forEach { it.exec(context) }
    }
}

@CorDslMarker
class CorChainDsl<T> : CorExecDsl<T>(), ICorChainDsl<T> {

    private val workers = mutableListOf<ICorExecDsl<T>>()

    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        blockOn = blockOn,
        blockExcept = blockExcept,
    )
}
