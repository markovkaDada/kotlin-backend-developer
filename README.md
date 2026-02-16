# Kotlin Backend Developer

Проект для курса OTUS Kotlin Backend Developer и персональных разработок.

## Структура проекта

Проект организован в виде трех композитных модулей (composite builds):

### 1. lessons
Учебные материалы курса OTUS.
- `m1l1-first` - Первый урок

### 2. swift-order
Персональный проект swift-order.
- `swift-order-api` - Модуль-заглушка (пример структуры)

### 3. build-plugin
Общие Gradle плагины для переиспользования настроек сборки.

## Сборка проекта

```bash
# Просмотр всех проектов
./gradlew projects

# Сборка всех модулей
./gradlew build

# Сборка конкретного модуля
./gradlew :lessons:m1l1-first:build
./gradlew :swift-order:build
./gradlew :build-plugin:build
```

## Требования

- JDK 21
- Gradle 8.10
- Kotlin 2.2.21

## Convention Plugins

Проект использует централизованные convention plugins из модуля `build-plugin`:
- `jvm-convention` - для JVM модулей (Kotlin JVM, тесты, зависимости)
- `multiplatform-convention` - для Kotlin Multiplatform модулей

Все подмодули используют JVM toolchain 21 и единые настройки компиляции.

Подробнее: [build-plugin/README.md](build-plugin/README.md)
