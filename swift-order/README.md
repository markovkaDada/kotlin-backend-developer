# Swift Order

Композитный модуль для проекта swift-order.

## Структура проекта

- `swift-order-api` - API и контракты (модуль-заглушка для примера)

### Планируемые модули

- `swift-order-common` - Общие компоненты
- `swift-order-app` - Основное приложение
- `swift-order-lib-*` - Библиотечные модули

## Создание нового модуля

1. Создайте директорию для модуля:
   ```bash
   mkdir -p swift-order/swift-order-новый/src/main/kotlin/ru/otus/otuskotlin/yan/swiftorder/новый
   mkdir -p swift-order/swift-order-новый/src/test/kotlin/ru/otus/otuskotlin/yan/swiftorder/новый
   ```

2. Создайте `build.gradle.kts`:
   ```kotlin
   plugins {
       id("jvm-convention")  // или multiplatform-convention для KMP
   }

   // Все настройки применены через convention plugin!
   // Добавьте специфичные зависимости при необходимости:
   dependencies {
       implementation(project(":swift-order-common"))
   }
   ```

3. Добавьте модуль в `swift-order/settings.gradle.kts`:
   ```kotlin
   include("swift-order-новый")
   ```

4. Соберите модуль:
   ```bash
   ./gradlew :swift-order:swift-order-новый:build
   ```

## Convention Plugins

Все модули используют convention plugins из `build-plugin`:
- `jvm-convention` - для JVM модулей (сейчас: swift-order-api)
- `multiplatform-convention` - для Kotlin Multiplatform модулей (будущие модули)

Это обеспечивает единообразную конфигурацию и упрощает поддержку.

## Сборка

```bash
# Все модули swift-order
./gradlew :swift-order:build

# Конкретный модуль
./gradlew :swift-order:swift-order-api:build
```
