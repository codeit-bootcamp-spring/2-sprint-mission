FROM amazoncorretto:17 as build
WORKDIR /app

# Gradle 파일 복사
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Gradle 권한 설정
RUN chmod +x gradlew

# Gradle 의존성 캐시 활용
RUN ./gradlew dependencies

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -x test

# 실행 이미지
FROM amazoncorretto:17
VOLUME /tmp
COPY --from=build /app/build/libs/*.jar .
## 호스트 포트 노출
EXPOSE 80
## 프로젝트 정보
ENV PROJECT_NAME="discodeit" \
    PROJECT_VERSION="1.2-M8" \
    JVM_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar $PROJECT_NAME-$PROJECT_VERSION.jar"]