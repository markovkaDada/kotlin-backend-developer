package ru.otus.kotlin.buildplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Convention plugin для JVM модулей.
 * Настраивает стандартную конфигурацию для Kotlin JVM проектов.
 */
class JvmPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        // Применяем Kotlin JVM plugin
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        // Настраиваем группу
        group = rootProject.group

        // Добавляем репозитории
        repositories.apply {
            mavenCentral()
        }

        // Настраиваем Kotlin
        extensions.configure<KotlinJvmProjectExtension> {
            jvmToolchain(21)

            compilerOptions {
                // Включаем строгие предупреждения
                freeCompilerArgs.add("-Xjsr305=strict")
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }

        // Добавляем стандартные зависимости
        dependencies {
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib")
            add("testImplementation", "org.jetbrains.kotlin:kotlin-test")
            add("testImplementation", "org.jetbrains.kotlin:kotlin-test-junit5")
        }

        // Настраиваем задачи
        tasks.apply {
            withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                }
            }

            withType<Test>().configureEach {
                useJUnitPlatform()
            }
        }

        // Добавляем информационную задачу
        tasks.register("printJvmInfo") {
            group = "otus"
            description = "Print info about JVM project configuration"

            doLast {
                println("JVM project configured: ${project.path}")
                println("Project: ${project.path}")
                println("Dir: ${project.projectDir}")
                println("Gradle: ${project.gradle.gradleVersion}")
                println("Group: ${project.group}")
            }
        }

        println("JVM Convention Plugin applied to: ${project.path}")
    }
}
