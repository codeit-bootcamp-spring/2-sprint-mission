package com.sprint.mission.discodeit.domain.binarycontent.storage.s3;

import com.sprint.mission.discodeit.common.s3.S3Manager;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Manager s3Manager;

  @Value("${discodeit.storage.s3.presigned-url-expiration}")
  private long presignedUrlExpirationSeconds;
  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();
    s3Manager.put(key, bucket, bytes, "image/jpeg");

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();

    return s3Manager.get(key, bucket);
  }

  @Override
  public ResponseEntity<?> download(UUID binaryContentId) {
    String key = binaryContentId.toString();
    URI downloadUrl = s3Manager.createDownloadUrl(key, bucket, presignedUrlExpirationSeconds);

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(downloadUrl)
        .build();
  }

}
