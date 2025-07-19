package com.sprint.mission.discodeit.common.s3.impl;

import static com.sprint.mission.discodeit.common.filter.constant.logConstant.REQUEST_ID;

import com.sprint.mission.discodeit.common.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.common.s3.S3Adapter;
import com.sprint.mission.discodeit.common.s3.exception.S3UploadException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3AdapterImpl implements S3Adapter {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Async("uploadExecutor")
  @Override
  public CompletableFuture<PutObjectResponse> put(PutObjectRequest putRequest,
      RequestBody request) {
    try {
      PutObjectResponse putObjectResponse = s3Client.putObject(putRequest, request);
      return CompletableFuture.completedFuture(putObjectResponse);
    } catch (Exception e) {
      return CompletableFuture.failedFuture(new S3UploadException());
    }
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
