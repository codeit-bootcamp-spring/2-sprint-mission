FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# 1. wrapper 스크립트 및 JAR 파일 복사
COPY gradlew .
COPY gradle gradle

# 2. 프로젝트 설정 파일 복사
COPY settings.gradle .

# 3. 의존성 정의 복사
COPY build.gradle .

# 4. 애플리케이션 소스 코드 전체 복사 (가장 많이 바뀜)
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon

# --- 2단계: 실행 환경 (Runtime Stage) ---
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV SPRING_PROFILES_ACTIVE=prod
ENV JVM_OPTS=""

# build gradle 파일에 bootJar 설정을 통해 jar 파일의 이름이 ${PROJECT_NAME}-${PROJECT_VERSION}.jar로 만들어짐.
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 80번 포트로 설정
EXPOSE 80

ENTRYPOINT ["sh", "-c", "exec java -jar /app/app.jar"]
