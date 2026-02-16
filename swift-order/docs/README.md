# Swift Order

Бэкенд-сервис для заказа вырезки деталей на ЧПУ станках.

## Архитектура

Система построена по принципу разделения на зоны:

- **API Gateway** (Nginx / Spring Cloud Gateway) — единая точка входа: маршрутизация, аутентификация, rate limiting
- **Swift Order Backend** — чистая зона бэкенда:
  - `app` — REST API: CRUD клиентов и заказов, управление жизненным циклом (Kotlin, Spring Boot)
  - `Notification Service` — Kafka-consumer: отправка уведомлений клиентам (Kotlin, Spring Boot)
- **Infrastructure** — PostgreSQL, Apache Kafka, ELK Stack

Пользователи (Заказчик, Оператор ЧПУ, Админ) → API Gateway → Backend → Infrastructure.

## Структура проекта

```
swift-order/
├── app/                  # Основное приложение (REST API)
├── docker/               # Dockerfile и инструкции по сборке
├── docs/                 # Документация
│   ├── 01-biz/           # Бизнес-документация (аудитория, стейкхолдеры, требования)
│   ├── 02-analysis/      # Функциональные и нефункциональные требования
│   └── architecture.puml # C4 Container диаграмма
└── settings.gradle.kts
```

### Планируемые модули

- `common` — общие компоненты (модели, утилиты)
- `lib-*` — библиотечные модули

## Сборка

```bash
# Все модули swift-order
./gradlew :swift-order:build

# Модуль app
./gradlew :swift-order:app:build
```

## Docker

Сборка образа (из корня `kotlin-backend-developer/`):

```bash
docker build -f swift-order/docker/Dockerfile -t swift-order:latest ./swift-order
```

Подробнее — в [swift-order/docker/README.md](../docker/README.md).

## ELK Stack

Для логирования используется ELK 7.x. Запуск из корня проекта:

```bash
docker-compose up -d
```

| Сервис         | Порт  | Назначение                     |
|----------------|-------|--------------------------------|
| Elasticsearch  | 9200  | Хранение и поиск логов         |
| Logstash       | 5000  | Приём логов (TCP, JSON)        |
| Kibana         | 5601  | Визуализация и анализ логов    |

## Convention Plugins

Все модули используют convention plugins из `build-plugin`:
- `jvm-convention` — для JVM модулей (сейчас: app)
- `multiplatform-convention` — для Kotlin Multiplatform модулей (будущие модули)
