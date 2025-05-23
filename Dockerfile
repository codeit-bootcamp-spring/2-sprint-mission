FROM amazoncorretto:17-alpine as build
WORKDIR /workspace/app

COPY gradlew build.gradle settings.gradle ./
COPY gradle  ./gradle

RUN ./gradlew --no-daemon dependencies

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

FROM amazoncorretto:17-alpine as runtime
WORKDIR /app

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

EXPOSE 80

COPY --from=build /workspace/app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ./

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]