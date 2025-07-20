package com.sprint.mission.discodeit.service.async;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsyncBinaryContentService {

  private final BinaryContentProcessor binaryContentProcessor; // 핵심 로직 위임

  @Async
  @Retryable(
      value = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000),
      recover = "recoverUpload"
  )
  public void uploadBinaryContent(UUID contentId, byte[] bytes) {
    try {
      binaryContentProcessor.processUpload(contentId, bytes);
      binaryContentProcessor.updateStatusToSuccess(contentId);
    } catch (InterruptedException e) {
      log.error("Upload operation was interrupted for ID {}. Propagating as RuntimeException.",
          contentId);
      Thread.currentThread().interrupt(); // 인터럽트 상태 복원
      throw new RuntimeException("Operation interrupted", e); // RuntimeException으로 감싸서 던짐
    }
  }

  @Recover
  public void recoverUpload(Exception e, UUID contentId, byte[] bytes) {
    String taskName = "AsyncBinaryContentService.uploadBinaryContent";
    log.error("Upload failed permanently for ID {}: {}", contentId, e.getMessage());

    binaryContentProcessor.handleFailure(contentId, taskName, e.getMessage());
  }
}
