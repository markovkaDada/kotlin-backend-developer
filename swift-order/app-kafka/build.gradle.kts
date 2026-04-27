plugins {
    id("jvm-convention")
    kotlin("plugin.spring")
}

dependencies {
    // --- Internal modules ---
    implementation(project(":app-common"))
    implementation(project(":api-v1"))
    implementation(project(":mappers"))

    // --- Spring (via BOM) ---
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.kafka)
    implementation(libs.spring.boot.autoconfigure)

    // --- Kotlin ---
    implementation(libs.kotlinx.coroutines.core)

    // --- Libraries ---
    implementation(libs.jackson.module.kotlin)

    // --- Test ---
    testImplementation(project(":biz"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.mockito.kotlin)
}
