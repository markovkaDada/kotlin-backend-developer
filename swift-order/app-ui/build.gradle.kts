plugins {
    id("jvm-convention")
    application
}

application {
    mainClass.set("ru.otus.otuskotlin.yan.swiftorder.appui.MainKt")
}

dependencies {
    // --- Internal modules ---
    implementation(project(":api-v1"))
    implementation(project(":mappers"))
    implementation(project(":models"))

    // --- Ktor server ---
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.html.builder)

    // --- Ktor client ---
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)

    // --- Kafka ---
    implementation(libs.kafka.clients)

    // --- Jackson ---
    implementation(libs.jackson.module.kotlin)

    // --- Coroutines ---
    implementation(libs.kotlinx.coroutines.core)

    // --- Test ---
    testImplementation(platform(libs.spring.boot.bom))
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.spring.kafka.test)
}
