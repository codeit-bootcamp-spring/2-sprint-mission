# as build = as builder
# as를 소문자로 입력하면 약간의 warn이 발생함.
FROM amazoncorretto:17 AS builder
WORKDIR /app

# file 복사
# .dockerignore에서 걸러진 파일은 제외하고 컨텍스트 최소화하여 전송, 캐싱 비용 절감
COPY . .

# Gradle Wrapper 이용 빌드
RUN chmod +x gradlew \
 && ./gradlew clean bootJar -x test --no-daemon

# Run Stage
FROM amazoncorretto:17

# 환경 변수
# ARG는 빌드타임 변수
# ENV는 런타임 환경 변수(컨테이너 실행 중에도 접근 가능)
# ARG로 빌드 때 이름 결정 -> ENV로 노출(실행)
ARG  PROJECT_NAME=discodeit
ARG  PROJECT_VERSION=1.2-M8

ENV PROJECT_NAME=${PROJECT_NAME} \
    PROJECT_VERSION=${PROJECT_VERSION} \
    JVM_OPTS=""

# 작업 디렉터리
WORKDIR /app

# Artifacts 복사
# 빌드-타임 ARG 값으로 Jar 이름을 결정
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar
# 아래 방식이 버전 변경 필요 없이 빌드를 단순화할 수 있다.
# COPY --from=builder /app/build/libs/*.jar app.jar

# 네트워크 설정
EXPOSE 80

# 실행
ENTRYPOINT ["sh","-c","java $JVM_OPTS -jar /app/app.jar"]
