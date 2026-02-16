# Lessons

Композитный модуль для учебных материалов курса OTUS Kotlin Backend Developer.

## Структура

- `m1l1-first` - Первый урок, модуль 1

## Добавление новых уроков

Для добавления нового урока:

1. Создайте новую директорию:
   ```bash
   mkdir -p lessons/m1l2-новый-урок/src/main/kotlin
   mkdir -p lessons/m1l2-новый-урок/src/test/kotlin
   ```

2. Добавьте модуль в `lessons/settings.gradle.kts`:
   ```kotlin
   include("m1l2-новый-урок")
   ```

3. Создайте `build.gradle.kts`:
   ```kotlin
   plugins {
       id("jvm-convention")
   }

   // Все базовые настройки применены через convention plugin!
   // Можно добавить специфичные зависимости:
   dependencies {
       implementation("com.example:library:1.0.0")
   }
   ```

4. Соберите модуль:
   ```bash
   ./gradlew :lessons:m1l2-новый-урок:build
   ```

## Convention Plugin

Все модули используют `jvm-convention` plugin, который автоматически настраивает:
- Kotlin JVM с toolchain 21
- Стандартные зависимости (kotlin-stdlib, kotlin-test)
- JUnit 5 для тестов
- Строгие предупреждения компилятора
