# Kotlin Backend Developer

Проект для курса OTUS Kotlin Backend Developer и персональных разработок.

## Структура проекта

Проект организован в виде трех композитных модулей (composite builds):

### 1. lessons
Учебные материалы курса OTUS.
- `m1l1-first` - Первый урок

### 2. swift-order
Персональный проект swift-order — бэкенд для заказа вырезки деталей на ЧПУ.
Модули:
- `models` — доменные модели (`SwiftOrder`, `SwiftOrderStatus`, ...)
- `api-v1` — DTO API v1 (генерация из OpenAPI)
- `api-log` — DTO для лог-событий
- `mappers` — преобразование API ↔ модель
- `biz` — бизнес-логика (CoR-цепочки операций)
- `app-common` — общие интерфейсы приложения (`Context`, `CorSettings`, `ISwiftOrderProcessor`)
- `app-spring` — REST-приложение на Spring Boot
- `app-kafka` — Kafka consumer/producer на Spring
- `app-ui` — Ktor UI с переключением транспортов HTTP/Kafka
- `repo-inmemory` — in-memory реализация репозитория
- `repo-postgres` — Postgres-реализация на Exposed
- `repo-tests` — общий набор тестов репозитория

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
