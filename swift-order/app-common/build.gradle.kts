plugins {
    id("jvm-convention")
}

dependencies {
    // --- Internal modules ---
    implementation(project(":models"))

    // --- Kotlin ---
    implementation(libs.kotlinx.datetime)

    // --- Libraries ---
    implementation(libs.slf4j.api)
}
