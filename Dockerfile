FROM amazoncorretto:17 AS build

WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./gradlew
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle

RUN chmod +x ./gradlew

COPY src ./src

RUN ./gradlew bootJar --no-daemon


FROM amazoncorretto:17-al2023-headless

WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8

ENV JVM_OPTS=""

COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "exec java ${JVM_OPTS} -Dspring.profiles.active=prod -jar app.jar"]
