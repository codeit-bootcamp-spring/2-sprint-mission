package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MesseageWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    // 클라이언트가 /pub/messages로 메시지 전송
    @MessageMapping("/messages")
    public void handleMessage(MessageCreateRequest request) {

        MessageDto messageDto = messageService.create(request, Collections.emptyList());

        // 구독자들에게 브로드 캐스트
        String destination = "/sub/channels." + request.channelId() + ".messages";
        messagingTemplate.convertAndSend(destination, messageDto);
    }
}