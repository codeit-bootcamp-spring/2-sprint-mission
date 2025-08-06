FROM amazoncorretto:17 AS build

WORKDIR /app

# Gradle wrapper와 설정 파일 먼저 복사 (캐시 최적화)
COPY gradle ./gradle
COPY gradlew ./gradlew
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle

# 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (소스 코드 변경 시 캐시 활용)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN ./gradlew bootJar --no-daemon --parallel

# 운영 이미지
FROM amazoncorretto:17-al2023-headless

# 보안을 위한 비루트 사용자 생성
RUN yum install -y shadow-utils && \
    groupadd -r appgroup && useradd -r -g appgroup appuser

WORKDIR /app

# 애플리케이션 정보
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=3.0-M12

# JVM 최적화 옵션
ENV JVM_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseStringDeduplication"

# 타임존 설정
ENV TZ=Asia/Seoul

# 필요한 디렉토리 생성
RUN mkdir -p /app/storage && \
    chown -R appuser:appgroup /app

# 빌드된 JAR 파일 복사
COPY --from=build --chown=appuser:appgroup /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 비루트 사용자로 실행
USER appuser

# 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:80/api/health || exit 1

EXPOSE 80

ENTRYPOINT ["sh", "-c", "exec java ${JVM_OPTS} -Dspring.profiles.active=prod -jar app.jar"]
