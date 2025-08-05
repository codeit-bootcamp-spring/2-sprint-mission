package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChatMessageKafkaEvent;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final KafkaTemplate<String, ChatMessageKafkaEvent> kafkaTemplate;

    @MessageMapping("/messages") // /pub/messages
    public void sendMessage(MessageCreateRequest request) {
        // Kafka로 발행
        ChatMessageKafkaEvent event = new ChatMessageKafkaEvent(
            request.channelId(),
            request.authorId()
        );
        kafkaTemplate.send("chat-messages", request.channelId().toString(), event);
    }
}
