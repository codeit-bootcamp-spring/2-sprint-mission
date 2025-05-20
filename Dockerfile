FROM amazoncorretto:17
WORKDIR /app

# Gradle 관련 파일 복사 (Wrapper 포함)
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Gradle 의존성 사전 다운로드 (캐시 활용)
RUN chmod +x gradlew && ./gradlew dependencies

# 애플리케이션 소스 코드 복사 및 빌드 (테스트 생략)
COPY src src
RUN ./gradlew build -x test && ls -l build/libs


# 환경 변수 설정 (기본값 포함)
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

# 임시 파일 저장소 볼륨 선언 (Spring Boot에서 종종 필요)
VOLUME /tmp

# 빌드된 JAR 이름 조합 후 복사
RUN cp build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 80 포트 노출
ENV SERVER_PORT=80
EXPOSE 80

# 실행 명령 설정 (환경 변수 활용)
CMD ["sh", "-c", "java $JVM_OPTS -jar app.jar"]