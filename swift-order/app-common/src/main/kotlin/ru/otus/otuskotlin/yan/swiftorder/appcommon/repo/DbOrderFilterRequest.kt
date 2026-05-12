package ru.otus.otuskotlin.yan.swiftorder.appcommon.repo

import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId

data class DbOrderFilterRequest(
    val ownerIdFilter: SwiftOwnerId = SwiftOwnerId(""),
    val statusFilter: SwiftOrderStatus = SwiftOrderStatus.NONE,
)
