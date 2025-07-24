package com.sprint.mission.discodeit.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.event.NewMessageNotificationEvent;
import com.sprint.mission.discodeit.event.RoleChangedNotificationEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
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
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "notification.new-message", groupId = "notification-group")
  @RetryableTopic(
      attempts = "3",
      backoff = @Backoff(delay = 1000, multiplier = 2),
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
  )
  public void handleNewMessageNotification(String kafkaEvent) throws JsonProcessingException {
    NewMessageNotificationEvent event = objectMapper.readValue(kafkaEvent,
        NewMessageNotificationEvent.class);
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
      throw e;
    }
  }

  @KafkaListener(topics = "notification.role-changed", groupId = "notification-group")
  @RetryableTopic(
      attempts = "3",
      backoff = @Backoff(delay = 1000, multiplier = 2),
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
  )
  public void handleRoleChangedNotification(String kafkaEvent) throws JsonProcessingException {
    RoleChangedNotificationEvent event = objectMapper.readValue(kafkaEvent,
        RoleChangedNotificationEvent.class);
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
      throw e;
    }
  }

  @KafkaListener(topics = "notification.async-failed", groupId = "notification-group")
  @RetryableTopic(
      attempts = "3",
      backoff = @Backoff(delay = 1000, multiplier = 2),
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
  )
  public void handleAsyncFailedNotification(String kafkaEvent) throws JsonProcessingException {
    AsyncFailedNotificationEvent event = objectMapper.readValue(kafkaEvent,
        AsyncFailedNotificationEvent.class);
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


}