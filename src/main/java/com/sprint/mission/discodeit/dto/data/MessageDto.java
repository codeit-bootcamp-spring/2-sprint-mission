package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(
        UUID id,
        UUID channelId,
        UUID authorId,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static MessageDto from(Message message) {
        return new MessageDto(
                message.getId(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}