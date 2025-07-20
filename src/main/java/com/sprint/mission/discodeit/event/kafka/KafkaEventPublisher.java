package com.sprint.mission.discodeit.event.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

  private static final String TOPIC_NOTIFICATION = "notification-events";
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @TransactionalEventListener
  public void handleNotificationEvent(NotificationEvent event) {
    log.info("Spring Event 수신 -> Kafka 발행: receiverId={}, type={}", event.receiverId(),
        event.notificationType());
    try {
      String jsonPayload = objectMapper.writeValueAsString(event); // 이 부분이 추가/변경
      kafkaTemplate.send(TOPIC_NOTIFICATION, jsonPayload); // String으로 전송
    } catch (Exception e) {
      log.error("Kafka 메시지 발행 실패. event: {}, error: {}", event, e.getMessage());
    }
  }
}
