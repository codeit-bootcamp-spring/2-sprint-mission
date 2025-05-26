package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();

    s3Client.putObject(PutObjectRequest.builder().
            bucket(bucket).
            key(key).
            build(),
        RequestBody.fromBytes(bytes));
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();

    ResponseInputStream<GetObjectResponse> object = s3Client.getObject(
        GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build());
    return object;
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    String key = metaData.id().toString();
    String contentType = metaData.contentType();

    String presignedUrl = generatePresignedUrl(key, contentType);

    return ResponseEntity.status(HttpStatus.SEE_OTHER)
        .header(HttpHeaders.LOCATION, presignedUrl)
        .build();
  }

  @Override
  public void deleteById(UUID id) {
    String key = id.toString();

    s3Client.deleteObject(DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build());

    log.info("S3 객체 삭제 완료: {}", key);
  }

  private String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key)
        .responseContentType(contentType).build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest).signatureDuration(Duration.ofMinutes(5)).build();

    return s3Presigner.presignGetObject(presignRequest).url().toString();
  }
}
