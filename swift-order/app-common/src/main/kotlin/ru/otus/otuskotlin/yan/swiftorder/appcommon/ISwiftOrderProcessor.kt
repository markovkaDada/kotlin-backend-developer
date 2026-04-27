package ru.otus.otuskotlin.yan.swiftorder.appcommon

interface ISwiftOrderProcessor {
    suspend fun exec(ctx: Context)
}
