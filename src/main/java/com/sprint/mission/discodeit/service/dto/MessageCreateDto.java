package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageCreateDto(
        UUID userId,
        UUID channelId,
        String content,
        List<UUID> attachmentIds,
        List<BinaryContentDto> attachments
) {
    public Message convertCreateDtoToMessage( List<UUID> updatedAttachmentIds) {
        return new Message(userId, channelId, content, updatedAttachmentIds);
    }

    public static MessageCreateDto withoutFile(UUID userId, UUID channelId, String content){
        return new MessageCreateDto(userId, channelId, content, null, null);
    }
}
