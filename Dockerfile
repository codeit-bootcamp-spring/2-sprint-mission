# 빌드 환경설정
FROM amazoncorretto:17 as build
WORKDIR /app

# Gradle 파일 복사
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .


# 의존성 프로젝트 다운로드, Gradle 의존성 캐시 활용해서 빌드 속도 빠르게
RUN ./gradlew dependencies

# 소스 코드 복사 및 테스트 제외하고 빌드
COPY src src
RUN ./gradlew build -x test

# 실행 스테이지
# jar하고 자바만 들어갑니다.
FROM amazoncorretto:17

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

WORKDIR /app

# VOLUME /tmp
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

# 이미지 생성시 안에 들어감
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app/app.jar"]