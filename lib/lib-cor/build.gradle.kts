group = "ru.otus.otuskotlin.yan"

plugins {
    id("jvm-convention")
}

dependencies {
    // --- Test ---
    testImplementation(libs.kotlinx.coroutines.core)
}
