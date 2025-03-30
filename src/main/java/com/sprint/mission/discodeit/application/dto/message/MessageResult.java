package com.sprint.mission.discodeit.application.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResult(UUID messageId, UUID userId, String context, Instant createdAt, UUID channelId,
                            List<UUID> attachmentIds) {
    public static MessageResult fromEntity(Message message) {
        return new MessageResult(message.getId(), message.getUserId(), message.getContext(), message.getCreatedAt(), message.getChannelId(), message.getAttachmentIds());
    }
}
