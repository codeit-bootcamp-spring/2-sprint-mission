package com.sprint.mission.discodeit.domain.notification.event.listener;

import static com.sprint.mission.discodeit.common.config.CacheConfig.NOTIFICATION_CACHE_NAME;
import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;

import com.sprint.mission.discodeit.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.event.event.NotificationEvent;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationRepository notificationRepository;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;

  @CacheEvict(value = NOTIFICATION_CACHE_NAME, key = "#event.receiverId")
  @Async("notificationExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Retryable(
      retryFor = Exception.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleNotificationEvent(NotificationEvent event) {
    try {
      createNotification(event);
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Recover
  public void recover(Exception ex, NotificationEvent event) {
    String requestId = MDC.get(REQUEST_ID);
    String errorType = ex.getClass().getSimpleName();

    CompletableFuture.runAsync(() -> {
      AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure(
          "CREATE_NOTIFICATION_FROM_EVENT", requestId, errorType
      );
      asyncTaskFailureRepository.save(asyncTaskFailure);
    });
  }

  private Notification createNotification(NotificationEvent event) {
    Notification notification = new Notification(
        event.getReceiverId(),
        event.getTitle(),
        event.getContent(),
        event.getType(),
        event.getTargetId()
    );
    return notificationRepository.save(notification);
  }

}
