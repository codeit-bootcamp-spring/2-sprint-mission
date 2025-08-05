package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.event.kafka.KafkaMessagePublisher;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketMessageController {

  private final MessageService messageService;
  private final SimpMessagingTemplate messagingTemplate;
  private final KafkaMessagePublisher kafkaMessagePublisher;

  @MessageMapping("/messages")
  public void createMessageWithoutBinaryContent(MessageCreateRequest request) {
    log.info("웹소켓 메시지 생성 요청: {}", request);

    MessageDto createdMessage = messageService.createMessage(request, new ArrayList<>());

    // 1. SimpleBroker 사용
    String destination = String.format("/sub/channels.%s.messages", createdMessage.channelId());
    messagingTemplate.convertAndSend(destination, createdMessage);

    // 2. Kafka 사용
//    kafkaMessagePublisher.sendToKafka(createdMessage);
  }

}
