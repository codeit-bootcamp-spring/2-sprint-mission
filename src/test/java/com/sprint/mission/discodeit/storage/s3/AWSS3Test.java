package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

    private static S3Client s3Client;
    private static S3Presigner s3Presigner;
    private static String bucketName;

    @BeforeAll
    static void setup() throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(".env")) {
            props.load(input);
        }

        String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        bucketName = props.getProperty("AWS_S3_BUCKET");
        Region region = Region.of(props.getProperty("AWS_S3_REGION"));

        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );

        s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        s3Presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Test
    void testUpload() throws IOException {
        String key = "test/" + UUID.randomUUID() + ".txt";
        String content = "Hello from S3 Test!";
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                RequestBody.fromString(content)
        );
        System.out.println("Upload success: " + key);
    }

    @Test
    void testDownload() throws IOException {
        String key = "test/b12e3242-df1a-4d6a-9564-a541ca5d22f9.txt";
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());

        String downloaded = new String(response.readAllBytes());
        System.out.println("Downloaded content: " + downloaded);
    }

    @Test
    void testGeneratePresignedUrl() {
        String key = "test/b12e3242-df1a-4d6a-9564-a541ca5d22f9.txt";

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(b -> b
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest));

        System.out.println("Presigned URL: " + presignedRequest.url());
    }
}
