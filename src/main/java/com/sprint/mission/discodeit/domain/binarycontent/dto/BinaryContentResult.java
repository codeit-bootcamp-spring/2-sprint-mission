package com.sprint.mission.discodeit.domain.binarycontent.dto;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
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

    public static List<BinaryContentResult> fromEntity(List<BinaryContent> binaryContents) {
        if (binaryContents == null) {
            return null;
        }

        return binaryContents.stream()
                .map(BinaryContentResult::fromEntity)
                .toList();
    }

}
