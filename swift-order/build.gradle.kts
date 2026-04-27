plugins {
    base
    id("jvm-convention") apply false
    alias(libs.plugins.openapi.generator) apply false
}

group = "ru.otus.otuskotlin.yan.swiftorder"

tasks.named("build") {
    dependsOn(subprojects.map { "${it.path}:build" })
}
