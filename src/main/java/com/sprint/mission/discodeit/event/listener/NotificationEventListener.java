package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.service.notification.CreateNotificationParam;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.CreateNotificationEvent;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
@Slf4j
public class NotificationEventListener {

  private final BasicNotificationService notificationService;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("asyncExecutor")
  @Retryable(
      maxAttempts = 3,
      recover = "createAsyncTaskFailure",
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleCreateNotificationEvent(CreateNotificationEvent notificationEvent) {
    notificationService.create(CreateNotificationParam.builder()
        .type(notificationEvent.type())
        .content(notificationEvent.content())
        .title(notificationEvent.title())
        .receiverId(notificationEvent.receiverId())
        .targetId(notificationEvent.targetId())
        .build());
  }

  @Recover
  @Transactional
  public void createAsyncTaskFailure(Exception e, CreateNotificationEvent notificationEvent) {
    AsyncTaskFailure asyncTaskFailure = AsyncTaskFailure.builder()
        .failureReason(e.getMessage())
        .requestId(MDC.get("requestId") != null ? MDC.get("requestId").toString() : "UNKNOWN")
        .taskName("NotificationCreate")
        .build();

    asyncTaskFailureRepository.save(asyncTaskFailure);

    log.warn("Notification create retry finally failed - requestId: {}", MDC.get("requestId"));

    notificationService.create(CreateNotificationParam.builder()
        .type(NotificationType.ASYNC_FAILED)
        .receiverId(notificationEvent.receiverId())
        .title("백그라운드 작업이 실패했습니다.")
        .content(e.getMessage())
        .targetId(null)
        .build());
  }
}
