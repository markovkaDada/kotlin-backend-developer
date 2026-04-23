package ru.otus.otuskotlin.yan.swiftorder.appcommon.logging

import ru.otus.otuskotlin.marketplace.logging.common.LogWrapper
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class LoggerProvider (
    private val provider: (String) -> LogWrapper = { LogWrapper.DEFAULT }
) {
    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(loggerId: String): LogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(clazz: KClass<*>): LogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(function: KFunction<*>): LogWrapper = provider(function.name)
}