package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds,
        Instant createdAt,
        Instant updatedAt
) {
    public static MessageDto from(Message message) {
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getChannel() != null ? message.getChannel().getId() : null,
                message.getAuthor() != null ? message.getAuthor().getId() : null,
                message.getAttachmentIds().stream()
                    .map(BinaryContent::getId)
                    .toList(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}