package com.sprint.mission.discodeit.service.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record MessageUpdateDto(
        UUID id,
        UUID userId,
        UUID channelId,
        String content,
        List<UUID> attachmentIds,
        List<MultipartFile> attachments
) {
}
