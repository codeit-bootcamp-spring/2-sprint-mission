package com.sprint.mission.discodeit.service.dto.messagedto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
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
                message.getMessage(),
                message.getChannelId(),
                message.getSenderId(),
                message.getAttachmentIds()

                );
    }
}