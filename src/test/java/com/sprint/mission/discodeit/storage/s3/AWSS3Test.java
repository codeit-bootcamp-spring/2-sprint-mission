package com.sprint.mission.discodeit.storage.s3;

import java.net.URL;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

@Disabled
@Slf4j
@DisplayName("S3 API 테스트")
public class AWSS3Test {

    private static String accessKey;
    private static String secretKey;
    private static String region;
    private static String bucket;
    private S3Client client;
    private S3Presigner presigner;
    private String testKey;

    @BeforeAll
    static void loadEnv() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);
        }

        accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        region = props.getProperty("AWS_S3_REGION");
        bucket = props.getProperty("AWS_S3_BUCKET");

        if (accessKey == null || secretKey == null || region == null || bucket == null) {
            throw new IllegalStateException("AWS S3 설정이 .env파일에 올바르게 정리되지 않았습니다.");
        }
    }

    @BeforeEach
    void setUp() {
        client = S3Client.builder().
            region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey))
            )
            .build();

        presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .build();

        testKey = "test-" + UUID.randomUUID().toString();

    }

    @Test
    @DisplayName("S3에 파일을 업로드한다")
    void uploadToS3() {
        String content = "Hello from .env via Properties!";

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(testKey)
                .contentType("text/plain")
                .build();

            client.putObject(request, RequestBody.fromBytes(content.getBytes()));
            log.info("파일 업로드 성공: {}", testKey);
        } catch (S3Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("S3에서 파일을 다운로드한다")
    void downloadTest() {
        //테스트를 위한 파일 먼저 업로드
        String content = "Test content for download";
        PutObjectRequest uploadRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(testKey)
            .contentType("text/plain")
            .build();
        client.putObject(uploadRequest, RequestBody.fromString(content));

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(testKey)
                .build();

            String downloadedContent = client.getObjectAsBytes(request).asUtf8String();
            log.info("다운로드된 파일 내용: {}", downloadedContent);
        } catch (S3Exception e) {
            log.error("파일 다운로드 실패: {}", e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("S3 파일에 대한 Presigned URL을 생성한다")
    void generatePresignedUrl() {
        // 테스트를 위한 파일 먼저 업로드
        String content = "Test content for presigned URL";
        PutObjectRequest uploadRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(testKey)
            .contentType("text/plain")
            .build();
        client.putObject(uploadRequest, RequestBody.fromString(content));

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(testKey)
                .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest).build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            URL url = presignedRequest.url();

            log.info("생성된 Presigned URL: {}", url);
        } catch (S3Exception e) {
            log.error("Presigned URL 생성 실패: {}", e.getMessage());
            throw e;
        }
    }
}
