# Build Plugin

Композитный модуль для convention plugins проекта.

## Структура

```
build-plugin/
├── build.gradle.kts                    # Конфигурация с gradlePlugin {}
├── settings.gradle.kts                 # Настройки модуля
└── src/main/kotlin/ru/otus/kotlin/buildplugin/
    ├── JvmPlugin.kt                    # Plugin<Project> для JVM
    └── MultiplatformPlugin.kt          # Plugin<Project> для KMP
```

## Доступные плагины

### 1. jvm-convention

Convention plugin для JVM модулей (класс: `JvmPlugin`). Настраивает:
- Kotlin JVM plugin
- JVM toolchain 21
- Стандартные зависимости (kotlin-stdlib, kotlin-test, kotlin-test-junit5)
- JUnit 5 для тестов
- Строгие предупреждения компилятора
- Информационную задачу `printJvmInfo`

**Использование:**
```kotlin
plugins {
    id("jvm-convention")
}

// Все базовые настройки уже применены!
// Можно добавить специфичные зависимости:
dependencies {
    implementation("com.example:library:1.0.0")
}
```

**Пример:** `swift-order/swift-order-api/build.gradle.kts`

### 2. multiplatform-convention

Convention plugin для Kotlin Multiplatform модулей (класс: `MultiplatformPlugin`). Настраивает:
- Kotlin Multiplatform plugin
- JVM таргет с toolchain 21
- Базовые source sets (commonMain, commonTest, jvmTest)
- Стандартные зависимости (kotlin-stdlib, kotlin-test, kotlin-test-junit5)
- JUnit 5 для JVM тестов
- Информационную задачу `printKmpInfo`

**Использование:**
```kotlin
plugins {
    id("multiplatform-convention")
}

// Базовая конфигурация готова!
// Можно добавить другие таргеты:
kotlin {
    js(IR) {
        browser()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                // JS-специфичные зависимости
            }
        }
    }
}
```

## Как использовать в проектах

### Настройка композитного модуля

**1. В `settings.gradle.kts` композитного модуля:**

```kotlin
pluginManagement {
    // Подключаем build-plugin для использования convention plugins
    includeBuild("../build-plugin")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
```

**2. В корневом `build.gradle.kts` композитного модуля:**

```kotlin
plugins {
    // Делаем convention plugins доступными для всех подмодулей
    id("jvm-convention") apply false
    id("multiplatform-convention") apply false
}

group = "ru.otus.otuskotlin.yan.название"

subprojects {
    repositories {
        mavenCentral()
    }
}
```

**3. В `build.gradle.kts` подмодуля:**

```kotlin
plugins {
    id("jvm-convention")  // или multiplatform-convention
}

// Все базовые настройки уже применены!
```

## Создание новых плагинов

1. Создайте класс плагина в `src/main/kotlin/ru/otus/kotlin/buildplugin/`:
   ```kotlin
   class MyPlugin : Plugin<Project> {
       override fun apply(project: Project) = with(project) {
           // Настройка проекта
       }
   }
   ```

2. Зарегистрируйте плагин в `build.gradle.kts`:
   ```kotlin
   gradlePlugin {
       plugins {
           create("my-plugin") {
               id = "my-plugin"
               implementationClass = "ru.otus.kotlin.buildplugin.MyPlugin"
           }
       }
   }
   ```

3. Соберите: `./gradlew :build-plugin:build`

4. Используйте: `id("my-plugin")`

## Информационные задачи

- `:printJvmInfo` - показывает информацию о JVM проекте
- `:printKmpInfo` - показывает информацию о KMP проекте

**Пример:**
```bash
./gradlew :swift-order:swift-order-api:printJvmInfo
```

## Преимущества

- Нет дублирования конфигурации между модулями
- Централизованное управление настройками
- Type-safe конфигурация через Kotlin DSL
- Легко обновлять версии и настройки
- Плагины автоматически доступны через composite build
