package com.sprint.mission.discodeit.s3;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

@Disabled
public class S3Test {

    private String bucket;
    private S3Client s3Client;
    private AwsBasicCredentials awsBasicCredentials;
    private String key;

    @BeforeEach
    void setup() throws Exception {
        Properties props = new Properties();
        Path path = Paths.get("/Users/hwangjihwan/Downloads/IntelliJStorage/2-sprint-mission/.env");

        try (InputStream input = Files.newInputStream(path)) {
            props.load(input);
        }

        String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        String region = props.getProperty("AWS_S3_REGION");
        bucket = props.getProperty("AWS_S3_BUCKET");

        awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }

    @AfterEach
    void tearDown() {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(deleteRequest);
    }


    @DisplayName("S3에 파일 업로드 후, 다운로드를 해서 동일한 내용인지 확인합니다.")
    @Test
    void testUpload() throws IOException {
        // given
        key = UUID.randomUUID() + ".txt";
        String message = "Hello";
        byte[] content = message.getBytes();
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.putObject(putRequest, RequestBody.fromBytes(content));

        // when
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getRequest);
        String downloadContent = new String(s3Object.readAllBytes(), StandardCharsets.UTF_8);

        //then
        Assertions.assertThat(downloadContent).isEqualTo(message);
    }

    @DisplayName("S3의 presignedUrl을 받습니다.")
    @Test
    void testPresignedUrl() {
        // given
        key = UUID.randomUUID() + ".txt";
        byte[] content = UUID.randomUUID().toString().getBytes();
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.putObject(putRequest, RequestBody.fromBytes(content));

        // when
        Region region = Region.AP_NORTHEAST_2;
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider((StaticCredentialsProvider.create(awsBasicCredentials)))
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                builder -> builder.getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));
        URL url = presignedRequest.url();

        //then
        Assertions.assertThat(url).isNotNull();
    }

}
