FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-23-jdk -y
COPY src .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjfk:23-jdk-slim

EXPOSE 8080

COPY --from=build /target/ProjetoDalia-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
