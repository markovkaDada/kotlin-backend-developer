plugins {
    base
}

group = "ru.otus.otuskotlin.yan"
version = "0.0.1"

tasks.named("build") {
    dependsOn(
        gradle.includedBuild("lib").task(":build"),
        gradle.includedBuild("swift-order").task(":build"),
    )
}