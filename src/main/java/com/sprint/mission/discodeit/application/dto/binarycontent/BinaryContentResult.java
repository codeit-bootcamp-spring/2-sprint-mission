package com.sprint.mission.discodeit.application.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BinaryContentResult(UUID id, Instant createdAt, String name, String contentType,
                                  byte[] bytes) {

    public static BinaryContentResult fromEntity(BinaryContent binaryContent) {
        return new BinaryContentResult(binaryContent.getId(),
                binaryContent.getCreatedAt(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getBytes());
    }

    public static List<BinaryContentResult> fromEntity(List<BinaryContent> binaryContents) {
        return binaryContents.stream()
                .map(BinaryContentResult::fromEntity)
                .toList();
    }
}
