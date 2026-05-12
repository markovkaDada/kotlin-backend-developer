plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":app-common"))
    implementation(project(":models"))
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgresql)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":repo-tests"))
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit5)
}
