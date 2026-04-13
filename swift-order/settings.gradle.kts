pluginManagement {
    includeBuild("../build-plugin")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "swift-order"

include("app")
