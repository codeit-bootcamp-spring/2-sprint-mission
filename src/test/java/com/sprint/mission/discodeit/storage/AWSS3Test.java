package com.sprint.mission.discodeit.storage;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.time.Duration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

  private final String bucket = System.getenv("AWS_S3_BUCKET");
  private final S3Client s3 = S3Client.builder()
      .region(Region.of(System.getenv("AWS_S3_REGION")))
      .credentialsProvider(
          StaticCredentialsProvider.create(
              AwsBasicCredentials.create(
                  System.getenv("AWS_S3_ACCESS_KEY"),
                  System.getenv("AWS_S3_SECRET_KEY")
              )
          )
      ).build();

  @Test
  public void uploadTest() {
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucket)
        .key("test.txt")
        .build();

    s3.putObject(request, new File("src/test/resources/sample.txt").toPath());
  }

  @Test
  public void downloadTest() {
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucket)
        .key("test.txt")
        .build();

    s3.getObject(request, new File("downloaded.txt").toPath());
  }

  @Test
  public void presignedUrlTest() {
    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(System.getenv("AWS_S3_REGION")))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    System.getenv("AWS_S3_ACCESS_KEY"),
                    System.getenv("AWS_S3_SECRET_KEY")
                )
            )
        ).build();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key("test.txt")
        .build();

    PresignedGetObjectRequest presigned = presigner.presignGetObject(b -> b
        .signatureDuration(Duration.ofSeconds(600))
        .getObjectRequest(getObjectRequest)
    );

    System.out.println("URL: " + presigned.url());
  }
}
