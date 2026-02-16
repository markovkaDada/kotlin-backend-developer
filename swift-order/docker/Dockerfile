FROM gradle:8.10-jdk21 AS build

WORKDIR /app

COPY ../build.gradle.kts ../settings.gradle.kts ./
COPY ../app ./app
COPY ../../build-plugin ../build-plugin

RUN gradle :app:build --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
