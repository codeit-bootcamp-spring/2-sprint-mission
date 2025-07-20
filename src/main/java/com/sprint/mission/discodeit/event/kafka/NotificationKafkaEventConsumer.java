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
   * 'notification-events' 토픽을 구독하는 Kafka 리스너입니다. groupId는 application.yml에 설정된 값과 동일해야 합니다.
   * containerFactory는 Kafka 재시도/에러 처리 등을 커스텀 설정할 때 사용합니다. (아래 추가 설명 참조)
   */
  @SneakyThrows
  @KafkaListener(topics = "notification-events", groupId = "discodeit-group", containerFactory = "kafkaListenerContainerFactory")
  public void handleNotificationMessage(@Payload String message) { // String으로 받음
    log.info("Kafka 메시지 수신: {}", message);
    try {
      // JSON 문자열을 NotificationEvent 객체로 수동 변환
      NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

      log.info("Kafka 메시지 수신: receiverId={}, type={}", event.receiverId(),
          event.notificationType());
      // 메시지를 기반으로 실제 알림 생성 로직을 호출합니다.
      notificationService.createNotification(
          event.receiverId(),
          event.notificationType(),
          event.targetId(),
          event.notificationInfo()
      );
      log.info("알림 생성 성공: receiverId={}", event.receiverId());
    } catch (Exception e) {
      // Kafka Listener에서 예외가 발생하면 Spring Kafka의 기본 재시도 정책이 동작합니다.
      // 더 정교한 제어를 위해 아래 KafkaConsumerConfig를 설정하는 것이 좋습니다.
      log.error("알림 생성 처리 중 오류 발생. message: {}, error: {}", message, e.getMessage());
      // 예외를 다시 던져야 Spring Kafka의 ErrorHandler가 이를 감지하고 재시도/DLQ 전송 등을 처리할 수 있습니다.
      throw e;
    }
  }
}
