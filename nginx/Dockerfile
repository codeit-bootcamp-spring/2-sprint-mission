FROM nginx:alpine

# NGINX 설정 파일 복사
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# 프론트엔드 정적 파일을 복사할 디렉토리 생성
RUN mkdir -p /usr/share/nginx/html

# 백엔드에서 빌드된 정적 리소스 복사
COPY src/main/resources/static/ /usr/share/nginx/html/

# 포트 3000 노출
EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]