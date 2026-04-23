package ru.otus.otuskotlin.yan.swiftorder.apilog

data class CommonLogModel(
    val messageTime: String = "",
    val logId: String = "",
    val source: String = "",
    val order: SwiftOrderLogModel? = null,
    val errors: List<ErrorLogModel> = emptyList(),
)
