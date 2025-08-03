package com.sprint.mission.discodeit.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaMessagePublisher {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public void sendToKafka(MessageDto messageDto) {
    try {
      String payload = objectMapper.writeValueAsString(messageDto);
      kafkaTemplate.send("websocket.messages", payload);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Kafka 메시지 직렬화 실패", e);
    }
  }
}

