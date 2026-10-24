# Build the Spring Boot application
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

COPY src ./src

RUN ./mvnw -DskipTests clean package

# Run the application
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV SERVER_PORT=10000

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]