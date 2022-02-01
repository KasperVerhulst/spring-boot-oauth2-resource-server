FROM maven:3.6.2-jdk-11-slim AS build
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn clean package

FROM openjdk:11
USER 1111
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/target/resource-server-0.2.0.jar .
ENTRYPOINT java -jar /app/resource-server-0.2.0.jar
