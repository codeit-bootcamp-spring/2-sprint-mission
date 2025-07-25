package com.sprint.mission.discodeit.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketBroadcastListener {

  private final SimpMessagingTemplate messagingTemplate;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "message.new-message", groupId = "message-broadcast-group")
  public void broadcastNewMessage(String kafkaEvent) throws JsonProcessingException {
    MessageCreatedEvent event = objectMapper.readValue(kafkaEvent, MessageCreatedEvent.class);

    String destination = String.format("/sub/channels.%s.messages", event.channelId());

    messagingTemplate.convertAndSend(destination, event.messageDto());

    log.debug("웹소켓 메시지 브로드캐스트: channelId={}, messageId={}", event.channelId(),
        event.messageDto().id());
  }
}
