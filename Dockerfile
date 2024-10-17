FROM openjdk:17-jdk-slim

LABEL authors="socury"

ARG JAR_FILE=./build/libs/shemtong-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

COPY .env /app/.env

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]