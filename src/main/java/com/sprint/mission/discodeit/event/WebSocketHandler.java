package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler {

  private final ObjectMapper objectMapper;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @Async
  @KafkaListener(
      topics = "new-message",
      groupId = "#{T(java.util.UUID).randomUUID().toString()}"
  )
  public void handleMessage(String kafkaEvent) {
    try {
      log.debug("카프카 메시지 WebSocket 수신 시작 : {}", kafkaEvent);
      NewMessageEvent event = objectMapper.readValue(kafkaEvent, NewMessageEvent.class);
      MessageDto messageDto = event.messageDto();
      String dest = String.format("/sub/channels.%s.messages", messageDto.channelId());
      simpMessagingTemplate.convertAndSend(dest, messageDto);
    } catch (Exception e) {
      log.error("WebSocket 메시지 생성 알림 이벤트 처리 중 오류 발생 :", e);
    }
  }
}
