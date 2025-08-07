package com.sprint.mission.discodeit.chat;

import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.security.userDetails.CustomUserDetails;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

  private final SimpMessagingTemplate messagingTemplate;
  private final MessageService messageService;

  @MessageMapping("/messages")
  public void sendMessage(
      @Payload MessageCreateRequest request
  ) {
    MessageResult messageResult = messageService.create(request, null);

    String destination = "/sub/channels." + request.channelId() + ".messages";
    messagingTemplate.convertAndSend(destination, messageResult);
  }

}
