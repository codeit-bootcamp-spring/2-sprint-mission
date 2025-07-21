package com.sprint.mission.discodeit.common.s3.impl;

import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;

import com.sprint.mission.discodeit.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.common.s3.S3Adapter;
import com.sprint.mission.discodeit.common.s3.exception.S3UploadException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3AdapterImpl implements S3Adapter {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final int MAX_ATTEMPT = 3;

  @Retryable(
      retryFor = {SdkClientException.class, AwsServiceException.class},
      maxAttempts = MAX_ATTEMPT,
      backoff = @Backoff(delay = 1000, multiplier = 2.0)
  )
  @Async("uploadExecutor")
  @Override
  public CompletableFuture<PutObjectResponse> put(
      PutObjectRequest putRequest,
      RequestBody request
  ) {

    try {
      Thread.sleep(4000);
    } catch (InterruptedException ex) {
    }

    try {
      PutObjectResponse putObjectResponse = s3Client.putObject(putRequest, request);
      return CompletableFuture.completedFuture(putObjectResponse);
    } catch (SdkClientException | AwsServiceException uploadException) {
      throw uploadException;
    } catch (Exception exception) {
      return CompletableFuture.failedFuture(exception);
    }
  }

  @Recover
  public CompletableFuture<PutObjectResponse> recoverFromApiCall(Exception ex,
      PutObjectRequest putRequest, RequestBody request
  ) {
    saveFailureRecordAsync(ex);

    return CompletableFuture.failedFuture(new S3UploadException());
  }

  private void saveFailureRecordAsync(Exception ex) {
    String requestId = MDC.get(REQUEST_ID);
    String errorType = ex.getClass().getSimpleName();

    CompletableFuture.runAsync(() -> {
      AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure(
          "S3_UPLOAD", requestId, errorType
      );
      asyncTaskFailureRepository.save(asyncTaskFailure);
    });
  }

  @Override
  public InputStream get(GetObjectRequest getRequest) {
    return s3Client.getObject(getRequest);
  }

  @Override
  public URI createDownloadUrl(
      GetObjectRequest getObjectRequest,
      long expirationSeconds
  ) {
    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
        builder -> builder.getObjectRequest(getObjectRequest)
            .signatureDuration(Duration.ofSeconds(expirationSeconds))
    );

    return URI.create(presignedRequest.url().toString());
  }

}
