package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

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
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private S3Client s3;
    private S3Presigner presigner;

    public AWSS3Test() throws IOException{
        Properties props = new Properties();
        props.load(new FileInputStream(".env"));

        this.accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        this.secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        this.region = props.getProperty("AWS_S3_REGION");
        this.bucket = props.getProperty("AWS_S3_BUCKET");

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        this.presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public void upload(UUID binaryContentId, byte[] data) {
        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(binaryContentId.toString())
                        .build(),
                RequestBody.fromBytes(data)
        );
        System.out.println("업로드 완료: " + binaryContentId);
    }

    public byte[] download(UUID binaryContentId) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(binaryContentId.toString())
                .build();

        try (ResponseInputStream<GetObjectResponse> s3is = s3.getObject(getObjectRequest)) {
            return s3is.readAllBytes(); // Java 9 이상
        }
    }

    public String getPresignedUrl(UUID binaryContentId) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(binaryContentId.toString())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }
}