# 빌드 스테이지 (의존성, 소스 코드 등 무거운 것들 먼저 빌드 -> 이미지 레이어 캐시 고려)
FROM amazoncorretto:17 as build

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies

# 변경 빈도가 높으므로 아래쪽에 위치
COPY src src
RUN ./gradlew build

# 런타임 스테이지 (소스코드, 의존성 제외 빌드된 결과물만 복사, 가벼운 이미지)
# 최종 Docker 이미지에는 빌드된 결과물(예: JAR 파일)만 포함되고, 소스 코드, 빌드 도구(Gradle, Maven 등), 임시 파일, 의존성 캐시 등은 포함되지 않음.
FROM amazoncorretto:17

# EXPOSE는 문서화로 포트를 노출시키는 역할만 할 뿐, 실제 이미지의 컨테이너 포트를 80으로 빌드하고 싶다면, ENTRYPOINT에 --server.port=80으로 명시해줘야함
EXPOSE 80

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

WORKDIR /app
# build.gradle에 따로 세팅해주지 않는다면, 프로젝트명-version명.jar로 빌드됨 -> 이 빌드 파일을 app.jar라는 이름으로 복사
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 환경 변수 JVM_OPTS 에 설정된 JVM 옵션을 적용해서 app.jar 를 실행
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app/app.jar --server.port=80"]