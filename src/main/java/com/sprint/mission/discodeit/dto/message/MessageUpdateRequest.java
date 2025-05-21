package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(
        @NotBlank
        String newContent
) {
}
