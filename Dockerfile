FROM amazoncorretto:17 AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew dependencies

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

COPY src src
RUN ./gradlew build -x test

FROM amazoncorretto:17
COPY --from=build /app/build/libs/*.jar /${PROJECT_NAME}-${PROJECT_VERSION}.jar

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /${PROJECT_NAME}-${PROJECT_VERSION}.jar"]