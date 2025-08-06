package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWsController {

  private final MessageService messageService;

  @MessageMapping("/messages")
  public void sendMessage(MessageCreateRequest request) {
    messageService.create(request, new ArrayList<>());
  }

}
