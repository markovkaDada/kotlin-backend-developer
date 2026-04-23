package ru.otus.otuskotlin.yan.swiftorder.appcommon.logging

import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.logging.common.LogWrapper

class Slf4jLogWrapper(override val loggerId: String) : LogWrapper {
    private val logger = LoggerFactory.getLogger(loggerId)

    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        data: Any?,
        objs: Map<String, Any>?,
    ) {
        val fullMsg = buildString {
            append(msg)
            if (data != null) append(" | $data")
            if (objs != null) append(" | $objs")
        }
        when (level) {
            LogLevel.ERROR -> if (e != null) logger.error(fullMsg, e) else logger.error(fullMsg)
            LogLevel.WARN  -> if (e != null) logger.warn(fullMsg, e)  else logger.warn(fullMsg)
            LogLevel.INFO  -> logger.info(fullMsg)
            LogLevel.DEBUG -> logger.debug(fullMsg)
            LogLevel.TRACE -> logger.trace(fullMsg)
        }
    }
}
