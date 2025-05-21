FROM amazoncorretto:17 as build
# docker 컨테이너 내부의 /app에 저장하라는 겨.
WORKDIR /app

# Gradle 파일 복사
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Gradle 의존성 캐시 활용
RUN chmod +x ./gradlew && ./gradlew dependencies

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -x test

FROM amazoncorretto:17
WORKDIR /app
VOLUME /tmp

# 컨테이너 내부에서 애플리케이션이 사용하는 포트 명시 (선언)
EXPOSE 80

# 환경 변수 설정
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# 실행 이미지
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

ENTRYPOINT ["sh","-c","java $JVM_OPTS -jar /app/app.jar"]