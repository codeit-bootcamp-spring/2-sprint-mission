package com.sprint.mission.discodeit.service.dto.messagedto;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageFindResponseDto(
        UUID id,
        String message,
        UUID channelId,
        UUID senderId,
        List<UUID> attachmentIds

) {
    public static MessageFindResponseDto fromMessage(Message message) {
        return new MessageFindResponseDto(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getAttachmentIds()

                );
    }
}