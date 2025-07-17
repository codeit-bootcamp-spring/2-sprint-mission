package com.sprint.mission.discodeit.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.CreateNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// Application에서 발행된 이벤트를 받아서 Kafka 메시지로 변환해 전송하는 핸들러
// 그 후, Kafka의 메시지를 Consumer로 꺼내서 -> NotificationHandler가 처리
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaHandler {

  // Producer - 메시지를 Kafka 보내는데 사용 (Kafka의 메시지는 key-value로 구성 / key는 토픽의 어떤 파티션에 쓸지를 결정하는 값, value는 실제 데이터)
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  // TOPIC - 동일한 주제의 메시지(이벤트)를 논리적으로 묶는 단위 <-> 파티션: 하나의 토픽을 쪼개서 물리적으로 데이터를 저장하는 단위 (병렬 분산 처리를 위함)
  private static final String TOPIC = "discodeit.notification-create";

  // Kafka의 producer.send() 메서드는 프로듀서 내부적으로 비동기 전송을 수행 (호출은 메인스레드에서 되지만, 전송 완료를 기다리진 않으므로, 메인 스레드 블로킹 X)
  // + 무거운 작업이 아니라면 스레드를 굳이 분리할 필요가 없으므로, @Async를 잘 붙이지 않음
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(CreateNotificationEvent event) {
    // Spring event를 Kafka 메시지로 변환해 전송
    log.info("Received CreateNotificationEvent - type: {}", event.type());

    try {
      // 메시지가 String, String 타입이므로 이벤트를 JSON 문자열로 직렬화 (kafka 메시지로 변환)
      // 하지만, Kafka의 메시지는 바이트 배열만 가능, 브로커가 실제로 저장하고 전송하는 데이터는 모두 byte[]
      // yml에 StringSerializer 지정 (String -> 바이트 배열로 변환)
      String payload = objectMapper.writeValueAsString(event);
      kafkaTemplate.send(TOPIC,
          payload); // 토픽에 메시지 발송, key가 없기 때문에 round-robin 방식으로 파티션에 고르게 분산, 메시지를 같은 파티션에 넣어주려면 Key값 지정 필요
      log.info("Kafka message sent to topic [{}]: {}", TOPIC, payload);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize event: {}", e.getMessage(), e);
    }
  }
}
