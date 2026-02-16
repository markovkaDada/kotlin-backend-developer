package ru.otus.kotlin.buildplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin для Kotlin Multiplatform модулей.
 * Настраивает базовую конфигурацию для KMP проектов.
 */
class MultiplatformPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        // Применяем Kotlin Multiplatform plugin
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        // Настраиваем группу
        group = rootProject.group

        // Добавляем репозитории
        repositories.apply {
            mavenCentral()
        }

        // Настраиваем Kotlin Multiplatform
        extensions.configure<KotlinMultiplatformExtension> {
            // JVM toolchain настраивается на уровне extension
            jvmToolchain(21)

            // Настраиваем JVM таргет
            jvm()

            // Можно добавить другие таргеты по необходимости:
            // js(IR) {
            //     browser()
            //     nodejs()
            // }
            // linuxX64()
            // macosArm64()

            // Настраиваем source sets
            sourceSets.apply {
                val commonMain = getByName("commonMain")
                commonMain.dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-stdlib")
                }

                val commonTest = getByName("commonTest")
                commonTest.dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test")
                }

                val jvmTest = getByName("jvmTest")
                jvmTest.dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test-junit5")
                }
            }
        }

        // Настраиваем задачи
        tasks.apply {
            withType<Test>().configureEach {
                useJUnitPlatform()
            }
        }

        // Добавляем информационную задачу
        tasks.register("printKmpInfo") {
            group = "otus"
            description = "Print info about configured KMP targets"

            doLast {
                println("KMP project configured: ${project.path}")
                println("Project: ${project.path}")
                println("Dir: ${project.projectDir}")
                println("Gradle: ${project.gradle.gradleVersion}")
                println("Group: ${project.group}")
            }
        }

        println("KMP Convention Plugin applied to: ${project.path}")
    }
}
