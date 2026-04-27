plugins {
    base
}

group = "ru.otus.otuskotlin.yan"

tasks.named("build") {
    dependsOn(subprojects.map { "${it.path}:build" })
}
