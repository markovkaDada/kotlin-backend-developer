# Swift Order API

Модуль-заглушка для демонстрации структуры проекта swift-order.

## Структура

```
swift-order-api/
├── build.gradle.kts                    # Конфигурация сборки модуля
├── README.md                           # Описание модуля
└── src/
    ├── main/
    │   └── kotlin/
    │       └── ru/otus/otuskotlin/yan/swiftorder/api/
    │           └── ApiStub.kt          # Пример кода
    └── test/
        └── kotlin/                     # Тесты (когда появятся)
```

## Создание нового модуля

Чтобы создать новый модуль в swift-order:

1. Создайте директорию с именем модуля в `swift-order/`
2. Скопируйте `build.gradle.kts` из этого модуля
3. Создайте структуру `src/main/kotlin/`
4. Добавьте `include("имя-модуля")` в `swift-order/settings.gradle.kts`

## Сборка

```bash
# Из корня проекта
./gradlew :swift-order:swift-order-api:build

# Из директории swift-order
cd swift-order
../gradlew swift-order-api:build
```
