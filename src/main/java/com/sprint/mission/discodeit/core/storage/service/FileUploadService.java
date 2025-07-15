package com.sprint.mission.discodeit.core.storage.service;

import com.sprint.mission.discodeit.core.recover.service.RecoverService;
import com.sprint.mission.discodeit.core.storage.repository.BinaryContentStoragePort;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

  private final BinaryContentStoragePort binaryContentStorage;
  private final RecoverService recoverService;

  @Async("AsyncExecutor")
  @Retryable(
      retryFor = {IOException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000)
  )
  public CompletableFuture<UUID> uploadWithRetry(UUID id, byte[] bytes) throws IOException {
    log.info("[FileUploadService] 파일 업로드 시도 (ID: {})", id);
    // 테스트를 위해 의도적으로 예외 발생
    if (true) {
      log.warn("[FileUploadService] 의도적인 예외 발생! (ID: {})", id);
      throw new IOException("의도적인 스토리지 연결 실패!");
    }
    binaryContentStorage.put(id, bytes);
    return CompletableFuture.completedFuture(id);
  }

  @Recover
  public CompletableFuture<UUID> recoverFileUpload(IOException e, UUID id, byte[] bytes)
      throws Exception {
    log.error("[Recoverer] 파일 업로드 모든 재시도 실패! 최종 실패 처리.", e);

    recoverService.write(id, e.getMessage());

    return CompletableFuture.failedFuture(
        new UncheckedIOException(String.format("최종 업로드 실패 (ID: %s)", id), e)
    );
  }
}
