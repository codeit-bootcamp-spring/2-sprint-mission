# 1. Nginx 공식 이미지를 기반으로 시작
FROM nginx:alpine

# 2. 우리가 만든 nginx.conf 파일을 컨테이너의 설정 파일 위치로 복사
COPY ./nginx/nginx.conf /etc/nginx/conf.d/default.conf

# 3. 우리가 만든 frontend 폴더의 모든 내용을 Nginx가 웹페이지를 보여주는 기본 폴더로 복사
COPY ../src/main/resources/static /usr/share/nginx/html