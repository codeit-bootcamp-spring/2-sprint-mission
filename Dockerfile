FROM amazoncorretto:17-alpine AS build

WORKDIR /app

# Gradle 파일 복사
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Gradle 의존성 캐시 활용
RUN ./gradlew dependencies

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -x test

# 실행 이미지
FROM amazoncorretto:17-alpine

WORKDIR /app
VOLUME /tmp

# 환경 변수 설정 (런타임용)
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS="-Xms128m -Xmx256m"

# 빌드 결과 복사 (정적 경로 사용)
COPY --from=build /app/build/libs/discodeit-1.2-M8.jar /app/discodeit-1.2-M8.jar

# 포트 노출
EXPOSE 80

# 애플리케이션 실행 명령어
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app/$PROJECT_NAME-$PROJECT_VERSION.jar"]