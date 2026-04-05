# Build .jar
FROM maven:3.9.14-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

# Run .jar
FROM eclipse-temurin:21.0.1_12-jre-alpine

COPY --from=build /app/target/shipmentservice-0.0.1-SNAPSHOT.jar shipmentservice-0.0.1-SNAPSHOT.jar

EXPOSE 8082
ENTRYPOINT [ "java", "-jar", "/shipmentservice-0.0.1-SNAPSHOT.jar" ]
