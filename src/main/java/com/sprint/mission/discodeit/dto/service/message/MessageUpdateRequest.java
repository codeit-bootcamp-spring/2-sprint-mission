package com.sprint.mission.discodeit.dto.service.message;

import jakarta.validation.constraints.NotNull;

public record MessageUpdateRequest(
    @NotNull String newContent
) {

}