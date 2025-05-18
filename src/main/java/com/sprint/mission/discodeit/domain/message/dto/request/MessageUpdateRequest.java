package com.sprint.mission.discodeit.domain.message.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(
        @NotBlank String newContent
) {
}
