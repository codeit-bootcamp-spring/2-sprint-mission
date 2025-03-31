package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageInfoResponse(
        UUID id,
        UUID channelId,
        UUID authorId,
        String content,
        List<UUID> attachmentIds,
        Instant createdAt,
        Instant updatedAt

) {
    public static MessageInfoResponse of(Message message) {
        return new MessageInfoResponse(message.getId(), message.getChannelId(), message.getAuthorId(),
                message.getContent(), message.getAttachmentIds(), message.getCreatedAt(),
                message.getUpdatedAt());
    }
}
