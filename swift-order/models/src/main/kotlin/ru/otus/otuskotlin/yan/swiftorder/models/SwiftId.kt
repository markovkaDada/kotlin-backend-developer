package ru.otus.otuskotlin.yan.swiftorder.models

interface SwiftId {
    val asString: String
}

@JvmInline
value class SwiftOrderId(override val asString: String) : SwiftId

@JvmInline
value class SwiftOwnerId(override val asString: String) : SwiftId

@JvmInline
value class SwiftFileId(override val asString: String) : SwiftId
