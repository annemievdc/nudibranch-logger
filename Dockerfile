# ---- Stage 1: Build the app ----
FROM maven:3.9.11-amazoncorretto-24 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

# ---- Stage 2: Run the app ----
FROM amazoncorretto:24-alpine-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
