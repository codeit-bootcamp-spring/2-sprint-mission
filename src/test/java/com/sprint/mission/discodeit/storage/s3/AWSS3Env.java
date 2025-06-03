package com.sprint.mission.discodeit.storage.s3;

import io.github.cdimascio.dotenv.Dotenv;

public final class AWSS3Env {

    private static final Dotenv dotenv = Dotenv.configure()
        .ignoreIfMissing()   // CI 환경 등에서 .env 가 없을 경우
        .load();

    public static String accessKey() {
        return value("AWS_S3_ACCESS_KEY");
    }

    public static String secretKey() {
        return value("AWS_S3_SECRET_KEY");
    }

    public static String region() {
        return value("AWS_S3_REGION");
    }

    public static String bucket() {
        return value("AWS_S3_BUCKET");
    }

    // 이 부분 에러 발생할 수도....?
    private static String value(String key) {
        String v = dotenv.get(key, System.getenv(key));
        if (v == null || v.isBlank()) {
            throw new IllegalStateException(key + " 가 설정되지 않았습니다");
        }
        return v;
    }

    // 생성자를 private으로 선언하여 util class임을 명확히 한다.
    private AWSS3Env() {
    }
}
