FROM maven:3.9.9-eclipse-temurin-23 AS build
RUN apt-get update
RUN apt-get install openjdk-23-jdk -y
COPY src .

RUN apt-get install maven -y
RUN mvn clean install

FROM eclipse-temurin:23-jdk-alpine

EXPOSE 8080

COPY --from=build /target/ProjetoDalia-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

