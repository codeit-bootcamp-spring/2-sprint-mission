package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final String bucket;
  @Value("${AWS_S3_PRESIGNED_URL_EXPIRATION:600}")
  private int presignedUrlExpiration = 600;

  public S3BinaryContentStorage(
      @Value("${AWS_S3_ACCESS_KEY}") String accessKey,
      @Value("${AWS_S3_SECRET_KEY}") String secretKey,
      @Value("${AWS_S3_REGION}") String region,
      @Value("${AWS_S3_BUCKET}") String bucket
  ) {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    this.s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    this.bucket = bucket;
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(binaryContentId.toString())
            .build(),
        RequestBody.fromBytes(bytes)
    );
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();

    return s3Client.getObject(getObjectRequest);
  }

  public String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
        .getObjectRequest(getObjectRequest)
        .build();

    URL url = s3Presigner.presignGetObject(presignRequest).url();
    return url.toString();
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto metaData) {
    // PresignedUrl 활용
    String url = generatePresignedUrl(metaData.id().toString(), metaData.contentType());

    return ResponseEntity.status(302)  // Found (Redirect)
        .header(HttpHeaders.LOCATION, url)
        .build();
  }

  public S3Client getS3Client() {
    return s3Client;
  }



  public String getBucket() {
    return this.bucket;
  }
}