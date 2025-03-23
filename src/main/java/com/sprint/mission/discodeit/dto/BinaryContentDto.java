package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BinaryContentDto(
        @NotNull UUID id,
        @NotNull UUID userId,
        @NotNull String uploadFileName,
        @NotNull String storeFileName
) {
    public static BinaryContentDto from(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getUserId(),
                binaryContent.getUploadFileName(),
                binaryContent.getStoreFileName()
        );
    }
}
