package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.*;
import software.amazon.awssdk.services.s3.presigner.model.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AWSS3Test {

    private S3Client s3;
    private S3Presigner presigner;
    private final String bucket = AWSS3Env.bucket();

    @BeforeAll
    void setUp() {
        var credentials = AwsBasicCredentials.create(
            AWSS3Env.accessKey(),
            AWSS3Env.secretKey());

        s3 = S3Client.builder()
            .region(Region.of(AWSS3Env.region()))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();

        presigner = S3Presigner.builder()
            .region(Region.of(AWSS3Env.region()))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
    }

    @AfterAll
    void tearDown() {
        if (s3 != null) {
            s3.close();
        }
        if (presigner != null) {
            presigner.close();
        }
    }

    // 업로드
    @Test
    void uploadObject() throws Exception {
        String key = "test/" + UUID.randomUUID() + ".txt";
        String content = "Hello S3 Upload";
        PutObjectResponse response = s3.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("text/plain")
                .build(),
            RequestBody.fromString(content));

        assertEquals(200, response.sdkHttpResponse().statusCode());
        // 정리
        s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }

    // 다운로드
    @Test
    void downloadObject() throws Exception {
        // 1. 업로드
        String key = "test/" + UUID.randomUUID() + ".txt";
        String original = "다운로드 검증";
        s3.putObject(
            PutObjectRequest.builder().bucket(bucket).key(key).build(),
            RequestBody.fromString(original));

        // 2. 다운로드
        ResponseBytes<GetObjectResponse> bytes = s3.getObjectAsBytes(
            GetObjectRequest.builder().bucket(bucket).key(key).build());

        assertEquals(original, bytes.asUtf8String());

        // 3. 정리
        s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }

    // Presigned URL
    @Test
    void generatePresignedUrl() {
        String key = "docs/sample.pdf";

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .getObjectRequest(getObjectRequest)
            .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(presignRequest);
        URL url = presigned.url();

        assertNotNull(url);
        System.out.println("Presigned URL = " + url);
        // 실제로 브라우저에 붙여서 10분 안에 열리면 OK
    }
}
