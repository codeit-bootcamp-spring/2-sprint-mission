package com.sprint.mission.discodeit.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.event.BinaryContentCreateEvent;
import com.sprint.mission.discodeit.event.BinaryContentUploadFailureEvent;
import com.sprint.mission.discodeit.event.BinaryContentUploadSuccessEvent;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.NewMessageNotificationEvent;
import com.sprint.mission.discodeit.event.RoleChangedNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {


  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("kafkaTaskExecutor")
  public void handleNewMessageNotification(NewMessageNotificationEvent event) {
    log.info("Spring Event -> Kafka: NewMessageNotificationEvent, receiverId={}",
        event.receiverId());
    sendToKafka("notification.new-message", event, "새 메시지 알림");
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("kafkaTaskExecutor")
  public void handleRoleChangedNotification(RoleChangedNotificationEvent event) {
    log.info("Spring Event -> Kafka: RoleChangedNotificationEvent, receiverId={}",
        event.receiverId());
    sendToKafka("notification.role-changed", event, "권한 변경 알림");
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("kafkaTaskExecutor")
  public void handleAsyncFailedNotification(AsyncFailedNotificationEvent event) {
    log.info("Spring Event -> Kafka: AsyncFailedNotificationEvent, receiverId={}",
        event.receiverId());
    sendToKafka("notification.async-failed", event, "비동기 실패 알림");
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("kafkaTaskExecutor") //
  public void handleMessageCreated(MessageCreatedEvent event) {
    log.info("Spring Event -> Kafka: MessageCreatedEvent, channelId={}", event.channelId());
    sendToKafka("message.new-message", event, "새 메시지 브로드캐스트");
  }

  @EventListener
  @Async("kafkaTaskExecutor")
  public void handleBinaryContentUploadSuccess(BinaryContentUploadSuccessEvent event) {
    log.info("Spring Event -> Kafka: BinaryContentUploadSuccessEvent, binaryContentId={}",
        event.binaryContentId());
    sendToKafka("binary-content.upload-success", event, "파일 업로드 성공");
  }

  @EventListener
  @Async("kafkaTaskExecutor")
  public void handleBinaryContentUploadFailure(BinaryContentUploadFailureEvent event) {
    log.info("Spring Event -> Kafka: BinaryContentUploadFailureEvent, binaryContentId={}",
        event.binaryContentId());
    sendToKafka("binary-content.upload-failure", event, "파일 업로드 실패");
  }

  @EventListener
  @Async("kafkaTaskExecutor")
  public void handleAsyncTaskFailure(AsyncTaskFailureEvent event) {
    log.info("Spring Event -> Kafka: AsyncTaskFailureEvent, requestId={}", event.requestId());
    sendToKafka("async-task.failure", event, "비동기 작업 실패");
  }


  private void sendToKafka(String topic, Object event, String eventType) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(event);

      kafkaTemplate.send(topic, jsonMessage)
          .whenComplete((result, ex) -> {
            if (ex == null) {
              log.debug("{} Kafka 전송 성공: topic={}", eventType, topic);
            } else {
              log.error("{} Kafka 전송 실패: topic={}, error={}", eventType, topic, ex.getMessage());
            }
          });

    } catch (Exception e) {
      log.error("{} 직렬화 실패: {}", eventType, e.getMessage());
      kafkaTemplate.send(topic, event);
    }
  }
}