package com.sprint.mission.discodeit.application.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResult(UUID id, Instant createdAt, Instant updatedAt, UUID authorId, String content,
                            UUID channelId,
                            List<UUID> attachmentIds) {
    public static MessageResult fromEntity(Message message) {
        List<UUID> attachmentIds = message.getAttachments()
                .stream()
                .map(BinaryContent::getId)
                .toList();

        return new MessageResult(message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getUser().getId(),
                message.getContext(),
                message.getChannel().getId(),
                attachmentIds);
    }
}
