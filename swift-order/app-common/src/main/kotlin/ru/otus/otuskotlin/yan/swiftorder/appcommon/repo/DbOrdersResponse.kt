package ru.otus.otuskotlin.yan.swiftorder.appcommon.repo

import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder

sealed interface DbOrdersResponse

data class DbOrdersResponseOk(val data: List<SwiftOrder>) : DbOrdersResponse

data class DbOrdersResponseErr(val errors: List<SOError> = emptyList()) : DbOrdersResponse {
    constructor(err: SOError) : this(listOf(err))
}
