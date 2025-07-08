package com.sprint.mission.discodeit.common.s3.impl;

import com.sprint.mission.discodeit.common.s3.S3Manager;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3ManagerImpl implements S3Manager {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  public S3ManagerImpl(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region
  ) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;

    StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey));
    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(credentialsProvider)
        .build();
    this.s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(credentialsProvider)
        .build();
  }

  @Override
  public PutObjectResponse put(String key, String bucket, byte[] fileBytes, String fileType) {
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(fileType)
        .build();

    return s3Client.putObject(putRequest, RequestBody.fromBytes(fileBytes));
  }

  @Override
  public InputStream get(String key, String bucket) {
    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    return s3Client.getObject(getRequest);
  }

  @Override
  public URI createDownloadUrl(
      String key,
      String bucket,
      long expirationSeconds
  ) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();
    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
        builder -> builder.getObjectRequest(getObjectRequest)
            .signatureDuration(Duration.ofSeconds(expirationSeconds))
    );

    return URI.create(presignedRequest.url().toString());
  }

}
