package ru.otus.otuskotlin.yan.swiftorder.apilog

data class SwiftOrderLogModel(
    val requestId: String? = null,
    val operation: SwiftOrderLogOperation? = null,
    val requestOrder: OrderLog? = null,
    val responseOrder: OrderLog? = null,
    val responseOrders: List<OrderLog>? = null,
)

enum class SwiftOrderLogOperation {
    CREATE, READ, UPDATE, DELETE, SEARCH
}
