FROM amazoncorretto:17 as build

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies

COPY src src
RUN ./gradlew build

# 빌드 결과물만 복사 (가벼운 이미지)
FROM amazoncorretto:17

EXPOSE 80

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

WORKDIR /app
# build.gradle에 따로 세팅해주지 않는다면, 프로젝트명-version명.jar로 빌드됨 -> 이 빌드 파일을 app.jar라는 이름으로 복사
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 환경 변수 JVM_OPTS 에 설정된 JVM 옵션을 적용해서 app.jar 를 실행
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app/app.jar"]