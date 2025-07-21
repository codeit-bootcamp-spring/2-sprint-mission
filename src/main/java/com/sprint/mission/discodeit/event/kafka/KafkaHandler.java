package com.sprint.mission.discodeit.event.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.AsyncFailureEvent;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.event.RoleChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaHandler {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  private void send(String topic, Object payload) {
    try {
      String jsonPayload = objectMapper.writeValueAsString(payload);
      kafkaTemplate.send(topic, jsonPayload);
    } catch (Exception e) {
      log.error("카프카 메시지 전송에 실패 : {}", e.getMessage());
    }
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(NewMessageEvent event) {
    log.debug("메시지 생성 카프카 생성 시작 - id: {}", event.messageDto().id());
    send("new-message", event);
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(RoleChangedEvent event) {
    log.debug("사용자 권한 변경 카프카 생성 시작 - id: {}", event.userDto().id());
    send("role-changed", event);
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(AsyncFailureEvent event) {
    log.debug("비동기 작업 실패 카프카 생성 시작 - id: {}", event.failure().getId());
    send("async-failure", event);
  }
}
