package ru.otus.otuskotlin.yan.swiftorder.appcommon

import ru.otus.otuskotlin.yan.swiftorder.appcommon.logging.LoggerProvider
import ru.otus.otuskotlin.yan.swiftorder.appcommon.logging.Slf4jLogWrapper

class AppSettings(
    val processor: ISwiftOrderProcessor,
    val loggerProvider: LoggerProvider = LoggerProvider { Slf4jLogWrapper(it) },
)