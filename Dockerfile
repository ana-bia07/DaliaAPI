# Estágio 1: Build (Usando uma imagem que já vem com Maven e Java 23)
FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app

EXPOSE 8080

COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]