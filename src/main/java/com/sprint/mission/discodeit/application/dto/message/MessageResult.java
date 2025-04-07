package com.sprint.mission.discodeit.application.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResult(UUID id, Instant createdAt, Instant updatedAt, UUID authorId, String content,
                            UUID channelId,
                            List<UUID> attachmentIds) {
    public static MessageResult fromEntity(Message message) {
        return new MessageResult(message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getUserId(),
                message.getContext(),
                message.getChannelId(),
                message.getAttachmentIds());
    }
}
