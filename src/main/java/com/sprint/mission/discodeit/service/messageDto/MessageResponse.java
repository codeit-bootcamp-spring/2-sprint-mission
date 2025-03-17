package com.sprint.mission.discodeit.service.messageDto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class MessageResponse {
    private UUID messageId;
    private UUID channelId;
    private UUID userId;
    private String content;
    private Instant timestamp;
    private List<String> attachments;

    public MessageResponse(Message message, List<String> attachments) {
        this.messageId = message.getId();
        this.channelId = message.getChannelId();
        this.userId = message.getId();
        this.content = message.getContent();
        this.timestamp = message.getUpdatedAt();
        this.attachments = attachments;
    }
}