package com.sprint.mission.discodeit.application.dto.message;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(@NotBlank String newContent) {
}
