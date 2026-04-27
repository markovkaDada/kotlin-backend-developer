plugins {
    id("jvm-convention")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    kotlin("plugin.spring")
}

dependencies {
    // --- Internal modules ---
    implementation(project(":app-common"))
    implementation(project(":app-kafka"))
    implementation(project(":api-v1"))
    implementation(project(":mappers"))
    implementation(project(":biz"))

    // --- Spring ---
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.kafka)

    // --- Kotlin ---
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)

    // --- Libraries ---
    implementation(libs.jackson.module.kotlin)

    // --- Test ---
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.kafka.test)
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("swift-order-app.jar")
}
