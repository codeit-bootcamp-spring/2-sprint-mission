package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class S3BinaryContentStorage implements BinaryContentStorage {
  private final String accessKeyId;
  private final String secretKey;
  private final String region;
  private final String bucket;
  private final long presignedUrlExpirationSeconds;

  public S3BinaryContentStorage(String accessKeyId, String secretAccessKey, String region, String bucket, long presignedUrlExpirationSeconds) {
    this.accessKeyId = accessKeyId;
    this.secretKey = secretAccessKey;
    this.region = region;
    this.bucket = bucket;
    this.presignedUrlExpirationSeconds = presignedUrlExpirationSeconds;
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    getS3Client().putObject(putRequest, RequestBody.fromBytes(bytes));
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();

    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    return getS3Client().getObject(getRequest);
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    String presignedUrl = generatePresignedUrl(metaData.id().toString(), metaData.contentType());
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(presignedUrl));
    return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
  }

  public S3Client getS3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(this.accessKeyId, this.secretKey);
    return S3Client.builder()
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .region(Region.of(this.region))
        .build();
  }

  public S3Presigner getS3Presigner() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretKey);
    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  public String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = getS3Presigner().presignGetObject(presignRequest);
    return presignedRequest.url().toString();
  }
}

