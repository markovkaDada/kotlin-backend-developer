pluginManagement {
    includeBuild("../build-plugin")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "lib"

include("lib-cor")
