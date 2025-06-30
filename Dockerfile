FROM amazoncorretto:17 AS builder

WORKDIR /app

# Gradle 복사
COPY gradle ./gradle
COPY gradlew ./gradlew

# 의존성 파일 복사 # 파일에 설정해둔 부분 복사
COPY build.gradle settings.gradle ./

# Gradle 캐시를 위한 의존성 파일 복사
RUN ./gradlew dependencies

# 소스 파일 복사 및 빌드
COPY src ./src
RUN ./gradlew build -x test


# 소스코드 복사 및 빌드, JRE만 실행
FROM amazoncorretto:17-alpine3.21

WORKDIR /app

# 프로젝트 정보를 ENV로 설정 # 해당 레이어에 저장
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8  \
    JVM_OPTS=""

## jar파일만 가져오기
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ./

# 80 포트 노출, 메타데이터
EXPOSE 80

# 실행
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
