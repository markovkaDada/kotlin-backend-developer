package ru.otus.otuskotlin.yan.swiftorder.models

enum class StubCase {
    NONE,
    SUCCESS,
    VALIDATION_BAD_ID,
    VALIDATION_BAD_DESCRIPTION,
    VALIDATION_BAD_AMOUNT,
    VALIDATION_BAD_OWNER_ID,
    VALIDATION_BAD_FILE_ID,
    DB_ERROR,
    NO_CASE,
}
