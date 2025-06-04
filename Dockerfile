
FROM amazoncorretto:17 AS builder

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .



# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드 (JAR 생성)
RUN chmod +x gradlew && ./gradlew bootJar -x test --no-daemon --stacktrace
# 이 시점에서 /app/build/libs/ 에 JAR 파일이 생성됨 (예: discodeit-1.2-M9.jar)


FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar application.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV JVM_OPTS="-Xmx384m -Xms256m -XX:MaxMetaspaceSize=64m -XX:+UseSerialGC"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "echo 'Running application with JVM_OPTS: $JVM_OPTS' && exec java $JVM_OPTS -jar application.jar"]