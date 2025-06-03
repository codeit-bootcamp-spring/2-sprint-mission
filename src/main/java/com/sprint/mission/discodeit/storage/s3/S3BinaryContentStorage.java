package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.core.content.controller.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3", matchIfMissing = false)
public class S3BinaryContentStorage implements BinaryContentStoragePort {

  private static final Logger logger = LoggerFactory.getLogger(S3BinaryContentStorage.class);

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final String bucketName;
  private final long presignedUrlExpirationSeconds;

  public S3BinaryContentStorage(
      S3Client s3Client,
      S3Presigner s3Presigner,
      @Value("${discodeit.storage.s3.bucket}") String bucketName,
      @Value("${discodeit.storage.s3.presigned-url-expiration}") long presignedUrlExpirationSeconds
  ) {
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
    this.bucketName = bucketName;
    this.presignedUrlExpirationSeconds = presignedUrlExpirationSeconds;
    logger.info("S3BinaryContentStorage initialized with injected S3Client and S3Presigner.");
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    String key = id.toString();
    try {
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(this.bucketName)
          .key(key)
          .build();
      s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
      logger.info("Successfully uploaded content with id: {} (key: {}) to S3 bucket: {}", id, key,
          this.bucketName);
      return id;
    } catch (Exception e) {
      logger.error("Error uploading content with id: {} (key: {}) to S3 bucket: {}. Error: {}", id,
          key, this.bucketName, e.getMessage(), e);
      throw new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public InputStream get(UUID id) {
    String key = id.toString();
    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(this.bucketName)
          .key(key)
          .build();
      ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Client.getObject(getObjectRequest);
      logger.info("Successfully retrieved content stream with id: {} (key: {}) from S3 bucket: {}",
          id, key, this.bucketName);
      return s3ObjectStream;
    } catch (Exception e) {
      logger.error("Error retrieving content with id: {} (key: {}) from S3 bucket: {}. Error: {}",
          id, key, this.bucketName, e.getMessage(), e);
      throw new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto binaryContentDto) {
    String key = binaryContentDto.id().toString();
    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(this.bucketName)
          .key(key)
          .responseContentType(binaryContentDto.contentType())
          .responseContentDisposition(
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .build();

      GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofSeconds(this.presignedUrlExpirationSeconds))
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedRequest = this.s3Presigner.presignGetObject(
          getObjectPresignRequest);
      String presignedUrl = presignedRequest.url().toString();

      logger.info("Generated presigned URL for id: {} (key: {}), filename: {}, expiration: {}s",
          binaryContentDto.id(), key, binaryContentDto.fileName(),
          this.presignedUrlExpirationSeconds);

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create(presignedUrl));
      return new ResponseEntity<>(headers, HttpStatus.FOUND);

    } catch (Exception e) {
      logger.error("Error generating presigned URL for id: {} (key: {}). Error: {}",
          binaryContentDto.id(), key, e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}