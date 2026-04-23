package ru.otus.otuskotlin.yan.swiftorder.appkafka.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "swift-order")
class SwiftOrderTopicsProperties(
    var createTopics: Boolean = false,

    var createRequestTopic: Topic = Topic(name = "swift-order-create-request"),
    var createResponseTopic: Topic = Topic(name = "swift-order-create-response"),

    var readRequestTopic: Topic = Topic(name = "swift-order-read-request"),
    var readResponseTopic: Topic = Topic(name = "swift-order-read-response"),

    var updateRequestTopic: Topic = Topic(name = "swift-order-update-request"),
    var updateResponseTopic: Topic = Topic(name = "swift-order-update-response"),

    var deleteRequestTopic: Topic = Topic(name = "swift-order-delete-request"),
    var deleteResponseTopic: Topic = Topic(name = "swift-order-delete-response"),

    var searchRequestTopic: Topic = Topic(name = "swift-order-search-request"),
    var searchResponseTopic: Topic = Topic(name = "swift-order-search-response"),
) {
    fun getAllTopics(): List<Topic> = listOf(
        createRequestTopic, createResponseTopic,
        readRequestTopic, readResponseTopic,
        updateRequestTopic, updateResponseTopic,
        deleteRequestTopic, deleteResponseTopic,
        searchRequestTopic, searchResponseTopic,
    )
}
