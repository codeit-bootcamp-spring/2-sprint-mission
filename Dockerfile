FROM amazoncorretto:17 AS build
WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8

# Gradle 파일 복사
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Gradle 의존성 캐시 활용
RUN ./gradlew dependencies

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -x test

# 실행 이미지
FROM amazoncorretto:17
WORKDIR /app
VOLUME /tmp
ENV JVM_OPTS=""
COPY --from=build /app/build/libs/*.jar /app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "/app.jar"]
