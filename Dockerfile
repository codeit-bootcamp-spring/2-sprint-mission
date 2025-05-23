FROM amazoncorretto:17 as build
WORKDIR /workspace/app

# Gradle 파일 및 소스 코드 복사
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle 의존성 캐시 활용 및 빌드
RUN ./gradlew dependencies
RUN ./gradlew build -x test

# 실행 이미지
FROM amazoncorretto:17
WORKDIR /app

# 환경 변수 설정
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# 외부 노출 포트번호 설정
EXPOSE 80

COPY --from=build /workspace/app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ./

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]