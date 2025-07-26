package com.sprint.mission.discodeit.domain.message.controller;

import com.sprint.mission.discodeit.domain.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.MessageDto;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketMessageController {

  private final SimpMessagingTemplate messagingTemplate;
  private final MessageService messageService;

  @MessageMapping("/messages")
  public void create(@Payload MessageCreateRequest messageCreateRequest) {
    log.info("웹소켓 메시지 수신: {}", messageCreateRequest);
    MessageDto createdMessage = messageService.create(messageCreateRequest, new ArrayList<>());
    String destination = "/sub/channels." + createdMessage.channelId() + ".messages";
    messagingTemplate.convertAndSend(destination, createdMessage);
  }
}
