# Docker — Swift Order

## Сборка образа

Команду нужно запускать из корня проекта `kotlin-backend-developer/`:

```bash
docker build -f swift-order/docker/Dockerfile -t swift-order:latest ./swift-order
```

## Запуск контейнера

```bash
docker run -p 8080:8080 swift-order:latest
```

## Запуск с ELK Stack

Сначала поднимите инфраструктуру из корня проекта:

```bash
docker-compose up -d
```

Затем запустите приложение:

```bash
docker run --network kotlin-backend-developer_elk -p 8080:8080 swift-order:latest
```

## Порты

| Сервис         | Порт  |
|----------------|-------|
| Swift Order    | 8080  |
| Elasticsearch  | 9200  |
| Logstash       | 5000  |
| Kibana         | 5601  |
