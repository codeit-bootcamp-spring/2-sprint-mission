# Amazon Corretto 17 사용
FROM amazoncorretto:17

# 작업 디렉토리
WORKDIR /app

# 프로젝트 파일 복사
COPY . .

# Gradle Wrapper 빌드
RUN ./gradlew clean build -x test

# 환경 변수
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS="-Xms512m -Xmx1024m"

# 80 포트 노출
EXPOSE 80

# 실행 명령
CMD ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar --spring.profiles.active=prod --server.port=80"]


