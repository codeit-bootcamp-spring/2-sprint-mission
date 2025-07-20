package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicAsyncBinaryContentStorageService {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Async
  @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
  @Transactional
  public void uploadAsyncWithRetry(UUID binaryContentId, byte[] bytes) {
    log.info("비동기 업로드 시작 bianryContentId={}", binaryContentId);

    binaryContentStorage.put(binaryContentId, bytes);

    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new IllegalStateException("Binary content not found"));

    binaryContent.markSuccess();

    binaryContentRepository.save(binaryContent);

    log.info("업로드 성공 bianryContentId={}", binaryContentId);
  }

  @Recover
  public void recoverUpload(Exception e, UUID binaryContentId, byte[] bytes) {
    log.error("업로드 실패 - 모든 재시도 실패 binaryContentId={}. reason={}", binaryContentId, e.getMessage());

    String requestId = MDC.get("requestId");

    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElse(null);

    if (binaryContent != null) {
      binaryContent.markFailed();
      binaryContentRepository.save(binaryContent);

      AsyncTaskFailure failure = new AsyncTaskFailure(
          "BinaryContentUpload",
          requestId != null ? requestId : "UNKNOWN",
          e.getMessage()
      );

      asyncTaskFailureRepository.save(failure);

      eventPublisher.publishEvent(
          new NotificationEvent(
              binaryContent.getUploaderId(),
              NotificationType.ASYNC_FAILED,
              binaryContentId
          )
      );
    } else {
      log.warn("BinaryContent 엔티티를 찾을 수 없어 알림 이벤트를 발행하지 못했습니다: binaryContentId={}", binaryContentId);
    }
  }
}
