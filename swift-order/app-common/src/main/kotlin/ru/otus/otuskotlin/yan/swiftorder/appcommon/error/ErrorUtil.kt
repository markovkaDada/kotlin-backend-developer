package ru.otus.otuskotlin.yan.swiftorder.appcommon.error

fun Throwable.asSOError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = SOError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)