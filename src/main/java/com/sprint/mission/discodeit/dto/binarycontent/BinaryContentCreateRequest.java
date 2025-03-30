package com.sprint.mission.discodeit.dto.binarycontent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record BinaryContentCreateRequest(
        @NotNull
        UUID fileId,

        @NotBlank
        String filePath,

        @NotBlank
        String fileName,

        @NotBlank
        String contentType,

        @Positive
        long fileSize
) {
}
