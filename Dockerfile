# ✅ Amazon Corretto 17 이미지 사용
FROM amazoncorretto:17

# ✅ 작업 디렉토리 설정
WORKDIR /app

# ✅ 빌드된 JAR 파일만 복사 (불필요한 전체 복사 X)
COPY build/libs/discodeit-1.2-M8.jar ./app.jar

# ✅ 환경 변수 설정 (기본값 또는 실행 시 설정됨)
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# ✅ 80 포트 노출
EXPOSE 80

# ✅ 애플리케이션 실행 명령어
CMD ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
