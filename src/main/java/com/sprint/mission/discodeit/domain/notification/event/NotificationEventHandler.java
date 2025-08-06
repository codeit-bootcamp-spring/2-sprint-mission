package com.sprint.mission.discodeit.domain.notification.event;

import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;

import com.sprint.mission.discodeit.common.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.common.failure.event.AsyncJobFailedEvent;
import com.sprint.mission.discodeit.security.auth.event.UserRoleChangedEvent;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.message.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

// TODO: 7/26/25 로직, 전략패턴으로 리펙터링 필요, 카프카에선 어떻게 적용할지 생각해야함
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

  private static final int NOTIFICATION_MAX_ATTEMPT = 2;
  private final NotificationRepository notificationRepository;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  @TransactionalEventListener
  @Async("notificationExecutor")
  @Retryable(
      retryFor = Exception.class,
      maxAttempts = NOTIFICATION_MAX_ATTEMPT,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handle(UserRoleChangedEvent event) {
    Notification notification = new Notification(
        event.userId(),
        "사용자 권한 변경 알림!",
        String.format("%s에서 %s로 역할이 변경되었습니다", event.previousRole(), event.updatedRole()),
        NotificationType.ROLE_CHANGED,
        event.userId()
    );
    saveNotification(notification);
  }

  @Recover
  public void recover(Exception ex, UserRoleChangedEvent event) {
    String requestId = MDC.get(REQUEST_ID);
    String errorType = ex.getClass().getSimpleName();

    AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure(
        "사용자 유저 변경 알림 생성 실패", requestId, errorType
    );
    asyncTaskFailureRepository.save(asyncTaskFailure);
  }

  @TransactionalEventListener
  @Async("notificationExecutor")
  @Retryable(
      retryFor = Exception.class,
      maxAttempts = NOTIFICATION_MAX_ATTEMPT,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handle(MessageCreatedEvent event) {
    Notification notification = new Notification(
        event.userId(),
        createNotificationTitle(event),
        createNotificationContent(event),
        NotificationType.ROLE_CHANGED,
        event.userId()
    );
    saveNotification(notification);
  }

  @Recover
  public void recover(Exception ex, MessageCreatedEvent event) {
    String requestId = MDC.get(REQUEST_ID);
    String errorType = ex.getClass().getSimpleName();

    AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure(
        "메세시 등록 알림 생성실패", requestId, errorType
    );
    asyncTaskFailureRepository.save(asyncTaskFailure);
  }

  private String createNotificationTitle(MessageCreatedEvent event) {
    String channelName = event.channelType().name();
    if (event.channelType().equals(ChannelType.PUBLIC)) {
      channelName = event.channelName();
    }
    return String.format("%s 채널에 메세지가 등록되었습니다!", channelName);
  }

  private String createNotificationContent(MessageCreatedEvent event) {
    String messageContent = "";
    if (event.channelType().equals(ChannelType.PUBLIC)) {
      messageContent = event.messageContent();
    }
    return messageContent;
  }

  @TransactionalEventListener
  @Async("notificationExecutor")
  @Retryable(
      retryFor = Exception.class,
      maxAttempts = NOTIFICATION_MAX_ATTEMPT,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handle(AsyncJobFailedEvent event) {
    Notification notification = new Notification(
        event.userId(),
        "비동기 작업 실패 알림!",
        createNotificationContent(event),
        NotificationType.ASYNC_FAILED,
        null
    );
    saveNotification(notification);
  }

  @Recover
  public void recover(Exception ex, AsyncJobFailedEvent event) {
    String requestId = MDC.get(REQUEST_ID);
    String errorType = ex.getClass().getSimpleName();

    AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure(
        "비동기 작업 실패", requestId, errorType
    );
    asyncTaskFailureRepository.save(asyncTaskFailure);
  }

  private static String createNotificationContent(AsyncJobFailedEvent event) {
    return String.format("%s 업로드 작업이 %s 문제로 실패했습니다.", event.JobType(),
        event.throwable().getClass().getName());
  }

  private void saveNotification(Notification notification) {
    Notification savedNotification = notificationRepository.save(notification);
    applicationEventPublisher.publishEvent(NotificationCreatedEvent.from(savedNotification));
  }

}
