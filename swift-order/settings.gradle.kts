pluginManagement {
    includeBuild("../build-plugin")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "swift-order"

include("app")
include("api-v1")
include("common")
include("mappers")
