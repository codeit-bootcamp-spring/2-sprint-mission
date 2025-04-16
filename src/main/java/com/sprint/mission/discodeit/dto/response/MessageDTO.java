package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record MessageDTO(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds
) {
    public static MessageDTO from(Message message) {
        List<UUID> attachmentIds = message.getAttachments().stream()
                .map(messageAttachment ->  messageAttachment.getAttachment().getId())
                .collect(Collectors.toList());

        return new MessageDTO(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                message.getAuthor() == null ? null : message.getAuthor().getId(),
                attachmentIds
        );
    }
}
