package com.sprint.mission.discodeit.storage.s3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
public class AWSS3Test {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get(".env")));

        String accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
        String region = properties.getProperty("AWS_S3_REGION");
        String bucket = properties.getProperty("AWS_S3_BUCKET");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider credentialsProvider =
            StaticCredentialsProvider.create(credentials);

        S3Client s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(credentialsProvider)
            .build();

        String key = "test.txt";
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("text/plain")
                .build()
        , RequestBody.fromString("test file test file test file test file test file test file test file test file .")); // 파일 업로드

        // 파일 다운로드 .
        S3Presigner s3Presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(credentialsProvider)
            .build();

        // 파일 업로드 .
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject( // Presigned GET 요청 생성
            builder -> builder // Presign 요청 빌더에 대한 람다 표현식 시작
                .signatureDuration(Duration.ofMinutes(5)) // 2. 서명의 유효 기간 설정
                .getObjectRequest( // 3. 실제 GetObject 요청에 대한 설정
                    getObjectRequestBuilder -> getObjectRequestBuilder
                        .bucket(bucket) // 대상 버킷
                        .key(key)       // 대상 객체 키
                        .build()
                )
                .build() // Presign 요청 빌더 완료
        );

        String presignedUrl = presignedRequest.url().toString();
        log.info("persigned url: {}", presignedUrl);

        // 파일 다운로드
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        ResponseBytes<GetObjectResponse> file = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes());
        log.info("file 내용: {}", new String(file.asByteArray()));
    }
}
