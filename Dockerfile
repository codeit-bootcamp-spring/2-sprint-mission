# 1단계: 빌드
FROM amazoncorretto:17 as builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test --no-daemon

# 2단계: 실행
FROM amazoncorretto:17

WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

CMD ["sh", "-c", "java $JVM_OPTS -jar app.jar"]