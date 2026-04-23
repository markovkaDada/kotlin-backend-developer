package ru.otus.otuskotlin.yan.swiftorder.appcommon

import ru.otus.otuskotlin.yan.swiftorder.appcommon.error.asSOError
import ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers.toLog
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import ru.otus.otuskotlin.yan.swiftorder.models.SOContextState
import kotlin.reflect.KClass
import kotlinx.datetime.Clock

suspend inline fun <T> AppSettings.controllerHelper(
    crossinline getRequest: suspend Context.() -> Unit,
    crossinline toResponse: suspend Context.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = loggerProvider.logger(clazz)
    val ctx = Context(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = SOContextState.FAILING
        ctx.errors.add(e.asSOError())
        processor.exec(ctx)
        if (ctx.command == Command.NONE) {
            ctx.command = Command.READ
        }
        ctx.toResponse()
    }
}
