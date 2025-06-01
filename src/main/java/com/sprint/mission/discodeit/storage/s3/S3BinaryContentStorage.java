package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3StorageProperties properties;

  private S3Client s3Client;
  private S3Presigner s3Presigner;

  @PostConstruct
  public void init() {
    AwsBasicCredentials creds = AwsBasicCredentials.create(
        properties.getAccessKey(),
        properties.getSecretKey()
    );

    this.s3Client = S3Client.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(creds))
        .build();

    this.s3Presigner = S3Presigner.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(creds))
        .build();
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(binaryContentId.toString())
        .contentType("application/octet-stream")
        .contentLength((long) bytes.length)
        .build();

    s3Client.putObject(request, RequestBody.fromBytes(bytes));
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(binaryContentId.toString())
        .build();

    return s3Client.getObject(request);
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(metaData.id().toString())
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofSeconds(properties.getPresignedUrlExpiration()))
        .build();

    URL presignedUrl = s3Presigner.presignGetObject(presignRequest).url();

    return ResponseEntity.status(302)
        .header("Location", presignedUrl.toString())
        .build();
  }
}
