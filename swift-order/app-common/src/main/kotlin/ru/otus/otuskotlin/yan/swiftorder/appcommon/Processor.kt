package ru.otus.otuskotlin.yan.swiftorder.appcommon

import ru.otus.otuskotlin.yan.swiftorder.appcommon.stubs.StubCreateSuccess
import ru.otus.otuskotlin.yan.swiftorder.appcommon.stubs.StubDeleteSuccess
import ru.otus.otuskotlin.yan.swiftorder.appcommon.stubs.StubReadSuccess
import ru.otus.otuskotlin.yan.swiftorder.appcommon.stubs.StubSearchSuccess
import ru.otus.otuskotlin.yan.swiftorder.appcommon.stubs.StubUpdateSuccess
import ru.otus.otuskotlin.yan.swiftorder.appcommon.error.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.SOContextState
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode

class Processor {

    fun exec(ctx: Context) {
        if (ctx.state == SOContextState.FAILING) return
        ctx.state = SOContextState.RUNNING
        when (ctx.workMode) {
            WorkMode.STUB -> execStub(ctx)
            WorkMode.PROD -> {
                ctx.errors.add(SOError(code = "not-implemented", message = "Production mode is not implemented yet"))
                ctx.state = SOContextState.FAILING
                return
            }
        }
        ctx.state = if (ctx.errors.isEmpty()) SOContextState.FINISHING else SOContextState.FAILING
    }

    private fun execStub(ctx: Context) {
        when (ctx.command) {
            Command.CREATE -> StubCreateSuccess.exec(ctx)
            Command.READ   -> StubReadSuccess.exec(ctx)
            Command.UPDATE -> StubUpdateSuccess.exec(ctx)
            Command.DELETE -> StubDeleteSuccess.exec(ctx)
            Command.SEARCH -> StubSearchSuccess.exec(ctx)
            Command.NONE   -> { /* no-op */ }
        }
    }
}
