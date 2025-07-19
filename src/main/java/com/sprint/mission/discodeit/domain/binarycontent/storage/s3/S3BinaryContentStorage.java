package com.sprint.mission.discodeit.domain.binarycontent.storage.s3;

import com.sprint.mission.discodeit.common.s3.S3Adapter;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private static final String UPLOAD_FILE_TYPE = "image/jpeg";

  private final S3Adapter s3Adapter;

  @Value("${discodeit.storage.s3.presigned-url-expiration}")
  private long presignedUrlExpirationSeconds;
  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(UPLOAD_FILE_TYPE)
        .build();

    s3Adapter.put(putRequest, RequestBody.fromBytes(bytes));

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();
    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    return s3Adapter.get(getRequest);
  }

  @Override
  public ResponseEntity<?> download(UUID binaryContentId) {
    String key = binaryContentId.toString();
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();
    URI downloadUrl = s3Adapter.createDownloadUrl(getObjectRequest, presignedUrlExpirationSeconds);

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(downloadUrl)
        .build();
  }

}
