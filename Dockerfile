FROM amazoncorretto:17 AS build
WORKDIR /app

# Gradle 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Gradle 의존성 캐시 활용
RUN ./gradlew dependencies

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -x test

# 실행 이미지
FROM amazoncorretto:17
VOLUME /tmp

# 환경 변수 설정
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app.jar --spring.profiles.active=prod"]