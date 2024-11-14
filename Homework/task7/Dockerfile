# Используем минимальный образ с JDK 17
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем скомпилированный JAR-файл из сборки Gradle
COPY build/libs/*.jar app.jar

# Открываем порт, используемый приложением
EXPOSE 8080

# Команда для запуска Spring Boot приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
