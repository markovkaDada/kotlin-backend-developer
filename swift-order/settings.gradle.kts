pluginManagement {
    includeBuild("../build-plugin")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("plugin.spring") version "2.2.21"
    }
}

rootProject.name = "swift-order"

include("api-v1")
include("api-log")
include("models")
include("mappers")
include("app-common")
include("app-spring")
include("app-kafka")
