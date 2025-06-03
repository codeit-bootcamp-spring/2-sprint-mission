package com.sprint.mission.discodeit.storage.s3;


import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String bucket;

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.bucket}") String bucket,
      S3Client s3Client,
      S3Presigner s3Presigner
  ) {
    this.bucket = bucket;
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
  }

  public UUID put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();

    try {
      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();

      s3Client.putObject(request, RequestBody.fromBytes(bytes));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return binaryContentId;
  }

  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();

    try {
      GetObjectRequest request = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();
      return s3Client.getObject(request);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public ResponseEntity<Void> download(BinaryContentDto metaData) {
    String key = metaData.id().toString();

    String presignedUrl = generatePresignedUrl(key, metaData.contentType());
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(presignedUrl))
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    try {
      GetObjectRequest getReq = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .responseContentType(contentType)
          .responseContentDisposition("attachment; filename=\"" + key + "\"")
          .build();

      PresignedGetObjectRequest presignedGet = s3Presigner.presignGetObject(
          GetObjectPresignRequest.builder()
              .getObjectRequest(getReq)
              .signatureDuration(Duration.ofMinutes(10))
              .build()
      );
      return presignedGet.url().toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
