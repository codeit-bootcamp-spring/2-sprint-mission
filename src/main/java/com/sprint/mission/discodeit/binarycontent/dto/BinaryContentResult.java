package com.sprint.mission.discodeit.binarycontent.dto;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResult(
        @NotNull
        UUID id,
        @NotNull
        Instant createdAt,
        @NotBlank
        String name,
        @NotBlank
        String contentType
) {

    public static BinaryContentResult fromEntity(BinaryContent binaryContent) {
        return new BinaryContentResult(
                binaryContent.getId(),
                binaryContent.getCreatedAt(),
                binaryContent.getFileName(),
                binaryContent.getContentType()
        );
    }

}
