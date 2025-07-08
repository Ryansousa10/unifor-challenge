FROM maven:3-openjdk-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests -Dquarkus.package.type=uber-jar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /workspace/target/*-runner.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
