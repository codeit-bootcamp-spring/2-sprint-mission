package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Properties properties;
  private final S3Client s3Client;
  private final S3Presigner s3presigner;

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    s3Client.putObject(
        PutObjectRequest.builder().bucket(properties.getBucket()).key(binaryContentId.toString())
            .build(), RequestBody.fromBytes(bytes));
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    return s3Client.getObject(
        GetObjectRequest.builder().bucket(properties.getBucket()).key(binaryContentId.toString())
            .build());
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    try {
      URI uri = new URI(generatePresignedUrl(metaData.id().toString(), metaData.contentType()));

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(uri);
      return ResponseEntity.status(302).headers(headers).build();
    } catch (URISyntaxException e) {
      return ResponseEntity.internalServerError().body("Invalid URL");
    }
  }

  public String generatePresignedUrl(String key, String contentType) {
    PresignedGetObjectRequest presignedRequest = s3presigner.presignGetObject(
        GetObjectPresignRequest.builder().getObjectRequest(
                GetObjectRequest.builder().bucket(properties.getBucket()).key(key)
                    .responseContentType(contentType).build()).signatureDuration(Duration.ofMinutes(10))
            .build());

    return presignedRequest.url().toString();
  }
}
