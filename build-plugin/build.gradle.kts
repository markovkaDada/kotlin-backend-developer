plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "ru.otus.otuskotlin.yan.plugin"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Kotlin Gradle Plugin из Version Catalog
    implementation(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("jvm-convention") {
            id = "jvm-convention"
            implementationClass = "ru.otus.kotlin.buildplugin.JvmPlugin"
        }
        create("multiplatform-convention") {
            id = "multiplatform-convention"
            implementationClass = "ru.otus.kotlin.buildplugin.MultiplatformPlugin"
        }
    }
}
