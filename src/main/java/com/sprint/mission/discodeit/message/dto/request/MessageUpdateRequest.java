package com.sprint.mission.discodeit.message.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(
        @NotBlank String newContent
) {
}
