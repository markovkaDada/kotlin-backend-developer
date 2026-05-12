plugins {
    id("jvm-convention")
}

dependencies {
    // --- External modules (composite build) ---
    implementation("ru.otus.otuskotlin.yan:lib-cor")

    // --- Internal modules ---
    implementation(project(":models"))
    implementation(project(":app-common"))

    // --- Test ---
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation(project(":repo-inmemory"))
    testImplementation(project(":repo-tests"))
}
