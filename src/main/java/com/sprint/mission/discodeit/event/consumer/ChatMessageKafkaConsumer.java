package com.sprint.mission.discodeit.event.consumer;

import com.sprint.mission.discodeit.dto.data.ChatMessageKafkaEvent;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatMessageKafkaConsumer {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
        topics = "chat-topic",
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory"
    )
    public void handleMessage(ChatMessageKafkaEvent event) {
        MessageDto dto = messageService.find(event.authorId());

        messagingTemplate.convertAndSend(
            "/sub/channels." + event.channelId() + ".messages",
            dto
        );
    }
}
