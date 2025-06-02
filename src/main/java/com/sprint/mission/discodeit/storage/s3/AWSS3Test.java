package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

    private String bucket;
    private S3Client s3;
    private S3Presigner presigner;

    @BeforeEach
    void setUp() throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Path.of(".env"))) {
            props.load(in);
        }

        String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        String region = props.getProperty("AWS_S3_REGION");
        this.bucket = props.getProperty("AWS_S3_BUCKET");

        s3 = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .build();

        this.presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .build();
    }

    // 업로드 테스트
    @Test
    void testUpload() {
        String key = "sample-test.txt";
        Path filePath = Path.of("src/test/resources/sample-test.txt");

        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        s3.putObject(request, filePath);
    }

    // 다운로드 테스트
    @Test
    void testDownload() {
        String key = "sample-test.txt";
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        s3.getObject(request, Path.of("src/test/resources/downloaded-test.txt"));
    }

    // PresignedUrl 생성 테스트
    // URL이 실제로 생성되는지 + URL 안에 버킷명과 키가 포함되는지 + 생성된 URL이 일정 시간 동안 유효한지
    @Test
    void testGeneratePresignedUrl() {
        String key = "sample-test.txt";

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
            GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(600))
                .getObjectRequest(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build())
                .build()
        );

        String url = presignedRequest.url().toString();
        System.out.println("Presigned URL: " + url);
        assertTrue(url.startsWith("https://"));
        assertTrue(url.contains(bucket));
        assertTrue(url.contains(key));

    }
}
