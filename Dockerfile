FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle build --dry-run || true
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENV JVM_OPTS="-Xms256m -Xmx512m"
EXPOSE 80
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
