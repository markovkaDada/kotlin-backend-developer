pluginManagement {
    includeBuild("../build-plugin")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "lessons"

include("m1l1-first")