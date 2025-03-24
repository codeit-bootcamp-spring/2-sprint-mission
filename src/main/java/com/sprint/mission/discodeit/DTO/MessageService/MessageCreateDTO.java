package com.sprint.mission.discodeit.DTO.MessageService;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public record MessageCreateDTO(
        String message,
        UUID userId,
        UUID channelId
) {
    public static MessageCreateDTO toDTO(Message message) {
        return new MessageCreateDTO(message.getMessage(), message.getUserId(), message.getChannelId());
    }

    public Message toEntity() {
        return new Message(this.message, this.userId, this.channelId);
    }
}
