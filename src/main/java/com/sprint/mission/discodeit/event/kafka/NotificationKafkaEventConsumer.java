package com.sprint.mission.discodeit.event.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaEventConsumer {

  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

  /**
   * 'notification-events' 토픽을 구독하는 Kafka 리스너
   */
  @SneakyThrows
  @KafkaListener(topics = "notification-events", groupId = "discodeit-group", containerFactory = "kafkaListenerContainerFactory")
  public void handleNotificationMessage(@Payload String message) { // String으로 받음
    log.info("Kafka 메시지 수신: {}", message);
    try {
      NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

      log.info("Kafka 메시지 수신: receiverId={}, type={}", event.receiverId(),
          event.notificationType());

      notificationService.createNotification(
          event.receiverId(),
          event.notificationType(),
          event.targetId(),
          event.notificationInfo()
      );
      log.info("알림 생성 성공: receiverId={}", event.receiverId());
    } catch (Exception e) {
      log.error("알림 생성 처리 중 오류 발생. message: {}, error: {}", message, e.getMessage());
      // 예외를 다시 던져야 Spring Kafka의 ErrorHandler가 이를 감지하고 재시도/DLQ 전송 등을 처리할 수 있음
      throw e;
    }
  }
}
