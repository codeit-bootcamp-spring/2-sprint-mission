package com.sprint.mission.discodeit.application.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResult(UUID id, Instant createdAt, String name, String contentType) {

    public static BinaryContentResult fromEntity(BinaryContent binaryContent) {
        return new BinaryContentResult(binaryContent.getId(),
                binaryContent.getCreatedAt(),
                binaryContent.getFileName(),
                binaryContent.getContentType());
    }
}
