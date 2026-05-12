package ru.otus.otuskotlin.yan.swiftorder.appcommon.repo

import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder

sealed interface DbOrderResponse

data class DbOrderResponseOk(val data: SwiftOrder) : DbOrderResponse

data class DbOrderResponseErr(val errors: List<SOError> = emptyList()) : DbOrderResponse {
    constructor(err: SOError) : this(listOf(err))
}
