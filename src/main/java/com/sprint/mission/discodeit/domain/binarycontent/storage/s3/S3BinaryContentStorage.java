package com.sprint.mission.discodeit.domain.binarycontent.storage.s3;

import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;

import com.sprint.mission.discodeit.common.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.binarycontent.storage.s3.exception.S3UploadArgumentException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private static final int MAX_ATTEMPT = 3;
  private static final String S3_UPLOAD_TASK = "S3_UPLOAD";

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;

  @Value("${discodeit.storage.s3.presigned-url-expiration}")
  private long presignedUrlExpirationSeconds;
  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Retryable(
      retryFor = {AwsServiceException.class, SdkClientException.class},
      notRecoverable = S3UploadArgumentException.class,
      maxAttempts = MAX_ATTEMPT,
      backoff = @Backoff(delay = 1000, multiplier = 2.0)
  )
  @Async("uploadExecutor")
  @Override
  public CompletableFuture<UUID> put(UUID binaryContentId, byte[] bytes) {
    log.debug("S3 비동기 요청 처리 쓰레드 {} ", Thread.currentThread().getName());
    if (binaryContentId == null || bytes == null || bytes.length == 0) {
      throw new S3UploadArgumentException(Map.of());
    }

    String key = binaryContentId.toString();
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(MediaType.IMAGE_JPEG_VALUE)
        .build();
    s3Client.putObject(putRequest, RequestBody.fromBytes(bytes));

    return CompletableFuture.completedFuture(binaryContentId);
  }

  @Recover
  public CompletableFuture<UUID> recoverS3Upload(SdkException sdkException, UUID binaryContentId,
      byte[] bytes
  ) {
    saveFailureRecordAsync(sdkException);
    throw sdkException;
  }

  private void saveFailureRecordAsync(Exception ex) {
    String requestId = MDC.get(REQUEST_ID);
    String errorType = ex.getClass().getSimpleName();

    AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure(S3_UPLOAD_TASK, requestId, errorType);
    asyncTaskFailureRepository.save(asyncTaskFailure);
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();
    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    return s3Client.getObject(getRequest);
  }

  @Override
  public ResponseEntity<?> download(UUID binaryContentId) {
    String key = binaryContentId.toString();
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
        builder -> builder.getObjectRequest(getObjectRequest)
            .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
    );

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(presignedRequest.url().toString()))
        .build();
  }

}
