package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@Primary
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;
  private final S3Properties properties;
  private final S3Presigner s3Presigner;

  @Override
  public UUID put(UUID id, byte[] data) {
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(id.toString())
        .build();
    s3Client.putObject(putRequest, RequestBody.fromBytes(data));
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(id.toString())
        .build();
    return s3Client.getObject(getRequest);
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto dto) {
    String presignedUrl = generatePresignedUrl(dto.id().toString(), dto.contentType());
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(presignedUrl))
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(properties.getPresignedUrlExpiration()))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
        presignRequest);

    return presignedGetObjectRequest.url().toString();
  }

  private S3Client getS3Client() {
    return S3Client.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())))
        .build();
  }
}
