package com.sprint.mission.discodeit.service.async;

import com.sprint.mission.discodeit.domain.AsyncTaskFailure;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.NotificationType;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.util.SecurityUtils;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentProcessor {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository; // 실패 기록도 중앙에서 관리

  /**
   * 실제 파일 저장 로직 (동기/비동기 공통 사용)
   */
  public void processUpload(UUID contentId, byte[] bytes) throws InterruptedException {
    // 의도적인 지연(3초)
    Thread.sleep(3000);
    binaryContentStorage.put(contentId, bytes);
  }

  /**
   * 성공 상태 업데이트 (새로운 트랜잭션으로 분리)
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateStatusToSuccess(UUID contentId) {
    BinaryContent content = binaryContentRepository.findById(contentId)
        .orElseThrow(() -> BinaryContentNotFoundException.byId(contentId));
    content.updateUploadStatus(BinaryContentUploadStatus.SUCCESS);
    binaryContentRepository.save(content);
  }

  /**
   * 실패 상태 업데이트 및 로그 기록 (새로운 트랜잭션으로 분리)
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleFailure(UUID contentId, String taskName, String failureReason) {
    // 1. 실패 상태 업데이트
    binaryContentRepository.findById(contentId).ifPresent(content -> {
      content.updateUploadStatus(BinaryContentUploadStatus.FAILED);
      binaryContentRepository.save(content);
    });

    // 2. 실패 기록 저장 (MDC에서 requestId 가져옴)
    String requestId = MDC.get("requestId");
    AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId, failureReason);
    asyncTaskFailureRepository.save(failure);

    // 3. 실패 알림 발행
    publishFailureNotification(taskName, failureReason);
  }

  private void publishFailureNotification(String method, String failureReason) {
    try {
      UUID userId = SecurityUtils.getCurrentUserId();
      eventPublisher.publishEvent(new NotificationEvent(
          userId,
          NotificationType.ASYNC_FAILED,
          null,
          Map.of(
              "method", method,
              "error", failureReason
          )
      ));
    } catch (Exception ex) {
      log.error("Failed to get current user ID for notification.", ex);
    }
  }
}
