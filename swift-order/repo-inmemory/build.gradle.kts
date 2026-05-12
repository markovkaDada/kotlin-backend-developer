plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":app-common"))
    implementation(project(":models"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":repo-tests"))
}
