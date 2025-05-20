FROM amazoncorretto:17 as build
WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies

COPY src src
RUN ./gradlew build -x test

FROM amazoncorretto:17

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

WORKDIR /app
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

CMD ["sh", "-c", "java $JVM_OPTS -Dspring.profiles.active=prod -jar app.jar"]