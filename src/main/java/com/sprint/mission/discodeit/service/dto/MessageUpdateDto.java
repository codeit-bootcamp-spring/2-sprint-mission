package com.sprint.mission.discodeit.service.dto;

import java.util.List;
import java.util.UUID;

public record MessageUpdateDto(
        UUID id,
        UUID userId,
        UUID channelId,
        String content,
        List<UUID> attachmentIds,
        List<BinaryContentDto> attachments
) {
    public static MessageUpdateDto withoutFile(UUID id, UUID userId, UUID channelId, String content){
        return new MessageUpdateDto(id, userId, channelId, content, null, null);
    }
}
