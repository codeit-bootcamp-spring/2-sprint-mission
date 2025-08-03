package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("messages")
    public void sendMessage(@Payload MessageCreateRequest request) {
        try{

            MessageDto savedMessage = messageService.create(request, null);
            log.debug("메시지 저장 완료: {}", savedMessage.id());

            messagingTemplate.convertAndSend("/sub/channels/" + savedMessage.channelId() + "/messages", savedMessage);


        }catch (Exception e){
            log.error("메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
