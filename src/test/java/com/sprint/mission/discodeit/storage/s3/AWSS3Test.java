package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class AWSS3Test {

  private static final String accessKey = System.getenv("AWS_S3_ACCESS_KEY");
  private static final String secretKey = System.getenv("AWS_S3_SECRET_KEY");
  private static final String region = System.getenv("AWS_S3_REGION");
  private static final String bucket = System.getenv("AWS_S3_BUCKET");

  private S3Client getClient() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .build();
  }

  @Test
  void upload() {
    S3Client s3 = getClient();
    String key = "test/" + UUID.randomUUID();

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType("text/plain")
        .build();

    s3.putObject(putRequest, RequestBody.fromString("Hello S3!"));
    System.out.println("Uploaded: " + key);
  }

  @Test
  void download() {
    S3Client s3 = getClient();
    String key = "test/dbc1e4a6-d3db-44a2-9f83-c0edfbec6176"; // 위에서 업로드한 key 입력

    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    File downloadTarget = Paths.get("downloaded.txt").toFile();
    s3.getObject(getRequest, downloadTarget.toPath());

    System.out.println("Downloaded to: " + downloadTarget.getAbsolutePath());
  }

  @Test
  void generatePresignedUrl() {
    // 다음 단계에서 따로 구현해줄 예정 (Presigner 필요)
    System.out.println("따로 분리해서 구현 예정");
  }
}