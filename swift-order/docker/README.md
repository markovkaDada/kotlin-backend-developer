# Docker — Swift Order

## Запуск всего стека

Из корня проекта `kotlin-backend-developer/`:

```bash
./gradlew :swift-order:app-spring:bootJar :swift-order:app-ui:installDist \
  && cd swift-order/docker \
  && docker-compose up --build
```

Это соберёт JAR Spring Boot и дистрибутив Ktor UI, затем поднимет все сервисы.

## Порты

| Сервис              | Порт | URL                          |
|---------------------|------|------------------------------|
| Swift Order Spring  | 8080 | http://localhost:8080        |
| **Swift Order UI**  | **8081** | **http://localhost:8081/orders** |
| Kafka               | 9092 | localhost:9092               |
| ZooKeeper           | 2181 | localhost:2181               |

Откройте **http://localhost:8081/orders** в браузере.  
В шапке переключайтесь между транспортами **HTTP** и **Kafka**.

## Сборка отдельных образов

Команды запускать из корня `kotlin-backend-developer/`:

```bash
# Spring Boot
docker build -f swift-order/docker/Dockerfile -t swift-order:latest ./swift-order

# Ktor UI
./gradlew :swift-order:app-ui:installDist
docker build -f swift-order/docker/Dockerfile.ui -t swift-order-ui:latest ./swift-order
```


./gradlew :swift-order:app-spring:bootJar :swift-order:app-ui:installDist \   
&& cd swift-order/docker \                                                  
&& docker-compose up --build