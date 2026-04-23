package ru.otus.otuskotlin.yan.swiftorder.appspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(
    basePackages = [
        "ru.otus.otuskotlin.yan.swiftorder.appspring",
        "ru.otus.otuskotlin.yan.swiftorder.appkafka",
    ]
)
class SwiftOrderApplication

fun main(args: Array<String>) {
    runApplication<SwiftOrderApplication>(*args)
}
