plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":models"))
    implementation(project(":api-v1"))
    implementation(project(":api-log"))
    implementation(project(":mappers"))

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
    implementation("org.slf4j:slf4j-api:2.0.16")
}
