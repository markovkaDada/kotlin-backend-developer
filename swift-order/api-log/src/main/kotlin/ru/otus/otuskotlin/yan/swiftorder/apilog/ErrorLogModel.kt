package ru.otus.otuskotlin.yan.swiftorder.apilog

data class ErrorLogModel(
    val message: String? = null,
    val field: String? = null,
    val code: String? = null,
    val level: String? = null,
)
