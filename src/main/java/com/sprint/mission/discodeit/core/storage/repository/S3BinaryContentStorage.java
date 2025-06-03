package com.sprint.mission.discodeit.core.storage.repository;

import com.sprint.mission.discodeit.core.storage.controller.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.core.storage.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
@Slf4j
public class S3BinaryContentStorage implements BinaryContentStoragePort {

  @Value("${aws.s3.expiration}")
  private Long expiration;

  @Value("${aws.s3.bucket}")
  private String bucket;

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final JpaBinaryContentRepository binaryContentRepository;

  public S3BinaryContentStorage(
      S3Client s3Client,
      S3Presigner s3Presigner,
      JpaBinaryContentRepository binaryContentRepository
  ) {
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
    this.binaryContentRepository = binaryContentRepository;
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(
        () -> new BinaryContentNotFoundException(ErrorCode.FILE_NOT_FOUND, id)
    );

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContent.getFileName())
        .contentType(binaryContent.getContentType())
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
    log.info("[S3BinaryContentStorage] Image uploaded successfully : {}", id);

    return id;
  }

  @Override
  public InputStream get(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(
        () -> new BinaryContentNotFoundException(ErrorCode.FILE_NOT_FOUND, id)
    );
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContent.getFileName())
        .build();
    return s3Client.getObject(getObjectRequest);
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    String presignedUrl = generatePresignedUrl(binaryContentDto.fileName(),
        binaryContentDto.contentType());

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(presignedUrl));
    log.info("[S3BinaryContentStorage] Image Download successfully");

    return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    if (expiration == null) {
      expiration = 600L;
    }

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(expiration))
        .getObjectRequest(getObjectRequest)
        .build();

    return s3Presigner.presignGetObject(presignRequest).url().toString();
  }
}
