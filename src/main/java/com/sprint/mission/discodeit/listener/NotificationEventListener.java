package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.event.NewMessageNotificationEvent;
import com.sprint.mission.discodeit.event.RoleChangedNotificationEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationService notificationService;
  private final ApplicationEventPublisher eventPublisher;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("notificationTaskExecutor")
  @Retryable(
      retryFor = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleNewMessageNotification(NewMessageNotificationEvent event) {
    log.info("새 메시지 알림 처리 시작: receiverId={}, channelId={}, messageId={}",
        event.receiverId(), event.channelId(), event.messageId());
    try {

      notificationService.createNewMessageNotification(
          event.receiverId(),
          event.channelId(),
          event.messageId(),
          event.authorName()
      );

      log.info("새 메시지 알림 생성 완료: receiverId={}", event.receiverId());

    } catch (Exception e) {
      log.error("새 메시지 알림 처리 실패: receiverId={}, channelId={}",
          event.receiverId(), event.channelId(), e);
      throw e;
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("notificationTaskExecutor")
  @Retryable(
      retryFor = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleRoleChangedNotification(RoleChangedNotificationEvent event) {
    log.info("권한 변경 알림 처리 시작: receiverId={}, oldRole={}, newRole={}",
        event.receiverId(), event.oldRole(), event.newRole());

    try {
      notificationService.createRoleChangedNotification(
          event.receiverId(),
          event.oldRole().toString(),
          event.newRole().toString()
      );

      log.info("권한 변경 알림 처리 완료: receiverId={}", event.receiverId());

    } catch (Exception e) {
      log.error("권한 변경 알림 처리 실패: receiverId={}", event.receiverId(), e);
      throw e;
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("notificationTaskExecutor")
  @Retryable(
      retryFor = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleAsyncFailedNotification(AsyncFailedNotificationEvent event) {
    log.info("비동기 작업 실패 알림 처리 시작: receiverId={}, taskName={}",
        event.receiverId(), event.taskName());

    try {
      notificationService.createAsyncFailedNotification(
          event.receiverId(),
          event.taskName(),
          event.failureReason()
      );

      log.info("비동기 작업 실패 알림 처리 완료: receiverId={}", event.receiverId());

    } catch (Exception e) {
      log.error("비동기 작업 실패 알림 처리 실패: receiverId={}", event.receiverId(), e);
      throw e;
    }
  }

  @Recover
  public void recover(Exception e, NewMessageNotificationEvent event) {
    eventPublisher.publishEvent(new AsyncTaskFailureEvent(
        "newMessageNotification",
        event.requestId(),
        e.getMessage()
    ));

    eventPublisher.publishEvent(
        new AsyncFailedNotificationEvent(
            event.receiverId(),
            "newMessageNotification",
            event.requestId(),
            e.getMessage()
        )
    );
  }

  @Recover
  public void recover(Exception e, RoleChangedNotificationEvent event) {
    eventPublisher.publishEvent(new AsyncTaskFailureEvent(
        "roleChangeNotification",
        event.requestId(),
        e.getMessage()
    ));

    eventPublisher.publishEvent(
        new AsyncFailedNotificationEvent(
            event.receiverId(),
            "roleChangeNotification",
            event.requestId(),
            e.getMessage()
        )
    );
  }

  @Recover
  public void recover(Exception e, AsyncFailedNotificationEvent event) {
    log.error("비동기 작업 실패 알림 처리 최종 실패: receiverId={}, taskName={}",
        event.receiverId(), event.taskName(), e);
  }

}