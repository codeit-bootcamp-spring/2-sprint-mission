FROM amazoncorretto:17 as build

ARG PROJECT_NAME=2-sprint-mission
ARG PROJECT_VERSION=1.2-M8
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"

WORKDIR /workspace/app

# Gradle 파일 복사
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# 의존성 설치
RUN ./gradlew dependencies -q --no-daemon

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -PprojectName=${PROJECT_NAME} -PprojectVersion=${PROJECT_VERSION} -x test -q

FROM amazoncorretto:17-alpine-jdk

ARG PROJECT_NAME=2-sprint-mission
ARG PROJECT_VERSION=1.2-M8

# 환경 변수 설정
ENV PROJECT_NAME=${PROJECT_NAME}
ENV PROJECT_VERSION=${PROJECT_VERSION}
ENV JVM_OPTS=""

WORKDIR /app

# 시스템 설정
VOLUME /tmp
EXPOSE 80

# 빌드 결과물 복사
COPY --from=build /workspace/app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 실행 명령
ENTRYPOINT ["sh", "-c", "exec java ${JVM_OPTS} -jar app.jar"]

