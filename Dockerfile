# Stage 1: Build Stage
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app

# Gradle 캐싱
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew
RUN ./gradlew dependencies

# 전체 소스코드 복사 후 빌드
COPY . .
RUN ./gradlew build -x test

# Stage 2: Runtime Stage
FROM amazoncorretto:17
WORKDIR /app

# 환경 변수 설정
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2.-M8
ENV JVM_OPTS=""

# 포트 노출
EXPOSE 80

# jar 파일만 app.jar로 복사
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 실행 명령어
ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JVM_OPTS -jar /app/app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE"]
