package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {

  private final MessageService messageService;

  @MessageMapping("/messages")
  public void send(MessageCreateRequest request) {
    log.debug("웹소켓 메시지 수신 : {}", request);
    MessageDto createdMessage = messageService.create(request, new ArrayList<>());
    log.debug("웹소켓 메시지 발신 :  {}", createdMessage);
  }
}
