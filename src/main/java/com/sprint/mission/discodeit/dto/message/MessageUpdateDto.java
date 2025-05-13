package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateDto(
        @NotBlank
        String newContent
) {
}
