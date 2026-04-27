plugins {
    id("jvm-convention")
}

dependencies {
    // --- Internal modules ---
    implementation(project(":api-v1"))
    implementation(project(":api-log"))
    implementation(project(":models"))
    implementation(project(":app-common"))

    // --- Kotlin ---
    implementation(libs.kotlinx.datetime)
}
