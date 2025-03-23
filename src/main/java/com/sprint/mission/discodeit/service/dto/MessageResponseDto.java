package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        UUID id,
        UUID userId,
        UUID channelId,
        String content,
        List<UUID> attachmentIds
) {
    public static MessageResponseDto convertToResponseDto(Message message) {
        return new MessageResponseDto(message.getId(), message.getUserId(), message.getChannelId(), message.getContent(), message.getAttachmentIds());
    }
}
