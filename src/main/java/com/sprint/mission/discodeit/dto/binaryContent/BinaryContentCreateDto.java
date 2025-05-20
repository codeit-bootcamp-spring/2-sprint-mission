package com.sprint.mission.discodeit.dto.binaryContent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateDto(
        @NotBlank
        @Size(max = 255)
        String fileName,

        @NotBlank
        @Size(max = 100)
        String contentType,

        @NotNull
        byte[] bytes
) {
}
