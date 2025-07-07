# 빌드 스테이지
FROM amazoncorretto:17 AS builder

WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./gradlew
COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies

COPY src ./src
RUN ./gradlew build -x test

# 런타임 스테이지
FROM amazoncorretto:17-alpine3.21

WORKDIR /app

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=2.0-M9 \
    JVM_OPTS=""

# ✅ 빌드 스테이지에서 jar 복사 (여기서 문제 해결됨!)
COPY --from=builder /app/build/libs/discodeit-${PROJECT_VERSION}.jar /app/app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "app.jar"]

