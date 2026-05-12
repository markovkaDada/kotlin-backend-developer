plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":app-common"))
    implementation(project(":models"))
    implementation(kotlin("test-junit5"))
    implementation(libs.kotlinx.coroutines.core)
}
