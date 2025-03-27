package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record MessageUpdateRequest(
        @NotBlank
        UUID id,
        @NotBlank
        String content
) {
}
