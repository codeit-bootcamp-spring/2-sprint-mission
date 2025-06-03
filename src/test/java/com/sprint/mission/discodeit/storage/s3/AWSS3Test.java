package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AWSS3Test {
    private S3Client client;
    private String bucket;
    private String key = "test";
    private String accessKey;
    private String secretKey;
    private String region;

    @BeforeEach
    void setUp() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(".env"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        this.accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        this.secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        this.region = props.getProperty("AWS_S3_REGION");
        this.bucket = props.getProperty("AWS_S3_BUCKET");

        client = S3Client.builder().
                region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @Test
    void uploadTest() {
        client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                RequestBody.fromString("test1")
        );
    }

    @Test
    void downloadTest() {
        ResponseBytes<GetObjectResponse> objectBytes = client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build()
        );
        String content = objectBytes.asUtf8String();
        System.out.println(content);

        assertEquals("test1", content);
    }

    @Test
    void generatePresignedUrl() {
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                builder -> builder.getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofSeconds(15))
        );

        String url = presignedRequest.url().toString();
        System.out.println("URL: " + url);

        assertTrue(url.startsWith("https://"));
    }
}
