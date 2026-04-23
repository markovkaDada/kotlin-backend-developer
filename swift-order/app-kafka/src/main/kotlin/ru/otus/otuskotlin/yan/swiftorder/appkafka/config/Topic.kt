package ru.otus.otuskotlin.yan.swiftorder.appkafka.config

data class Topic(
    var name: String = "",
    var partitions: Int = 1,
    var replicationFactor: Short = 1,
    var concurrency: Int = 1,
    var pollTimeout: Int = 5000,
)
