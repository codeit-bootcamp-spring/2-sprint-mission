#!/bin/sh

# exec로 JVM 옵션과 함께 JAR 실행 (모든 인자를 그대로 전달)
exec java $JVM_OPTS -jar /app/app.jar

