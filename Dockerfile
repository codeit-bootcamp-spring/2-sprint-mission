FROM amazoncorretto:17 as build
WORKDIR /app

COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle

RUN chmod +x ./gradlew
# ENV GRADLE_OPTS="-Xmx2048m -Xms512m"
RUN ./gradlew dependencies

COPY src src
ENV JAVA_TOOL_OPTIONS="-Xmx512m -Xms256m -XX:+UseSerialGC"
RUN ./gradlew build -x test --no-daemon

FROM amazoncorretto:17
WORKDIR /app

ARG PROJECT_NAME=discodeit
ARG PROJECT_VERSION=1.2-M8
ENV PROJECT_NAME=${PROJECT_NAME}
ENV PROJECT_VERSION=${PROJECT_VERSION}
ENV JVM_OPTS=""
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
