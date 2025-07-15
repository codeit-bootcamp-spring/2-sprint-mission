package com.sprint.mission.discodeit.core.storage.service;

import com.sprint.mission.discodeit.core.storage.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.core.storage.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.core.storage.repository.BinaryContentStoragePort;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final BinaryContentStoragePort binaryContentStorage;

  @Async
  @Retryable(
      retryFor = {IOException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000)
  )
  public CompletableFuture<UUID> uploadWithRetry(UUID id, byte[] bytes) {
    try {
      binaryContentStorage.put(id, bytes);
    } catch (IOException e) {
      log.error("[FileUploadService] 실행 실패! 재시도 중 (ID: {})", id, e.getCause());
      throw new UncheckedIOException(e);
    }
    return CompletableFuture.completedFuture(id);
  }

  @Recover
  @Transactional
  public CompletableFuture<Void> recover(UncheckedIOException e, UUID id, byte[] bytes) {
    log.error("[FileUploadService] 모든 재시도 실패! 최종 실패 처리. (ID: {})", id, e.getCause());
    String requestId = MDC.get("requestId");
    AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure("File Upload", requestId,
        e.getMessage());
    asyncTaskFailureRepository.save(asyncTaskFailure);
    return CompletableFuture.failedFuture(e.getCause());
  }
}
