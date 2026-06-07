# ================================================================
# Многоэтапная сборка (multi-stage build)
# Этап 1: сборка JAR с помощью Maven
# Этап 2: минимальный образ для запуска
# ================================================================

# ---------- Этап 1: сборка ----------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Копируем Maven Wrapper и pom.xml отдельным слоем —
# при изменении только исходников этот слой берётся из кэша Docker
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Исправляем line endings (Windows CRLF → Unix LF) и даём права на выполнение
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Загружаем зависимости (кэшируется, если pom.xml не изменился)
RUN ./mvnw dependency:go-offline -q

# Копируем исходный код и собираем JAR
COPY src/ src/
RUN ./mvnw package -DskipTests -q

# ---------- Этап 2: запуск ----------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копируем только собранный JAR из этапа сборки
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

# -Djava.security.egd ускоряет старт Spring Boot в контейнере,
# используя /dev/urandom вместо /dev/random
ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", "app.jar"]
