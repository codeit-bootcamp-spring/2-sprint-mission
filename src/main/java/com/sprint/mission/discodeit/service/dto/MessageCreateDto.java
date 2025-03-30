package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record MessageCreateDto(
        UUID userId,
        UUID channelId,
        String content,
        List<UUID> attachmentIds,
        List<MultipartFile> attachments
) {
    public Message convertCreateDtoToMessage(List<UUID> updatedAttachmentIds) {
        return new Message(userId, channelId, content, updatedAttachmentIds);
    }

}
