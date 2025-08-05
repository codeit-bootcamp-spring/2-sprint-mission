package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/messages")
    public void handleSend(MessageCreateRequest request) {
        MessageDto messageDto = messageService.create(request, List.of());

        // 구독 엔드포인트
        String destination = "/sub/channels." + request.channelId() + ".messages";
        messagingTemplate.convertAndSend(destination, messageDto); // STOMP 메시지 발행 -> 브로커에 넘김
    }
}
