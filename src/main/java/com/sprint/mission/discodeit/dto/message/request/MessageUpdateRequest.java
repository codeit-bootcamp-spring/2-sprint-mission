package com.sprint.mission.discodeit.dto.message.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageUpdateRequest(
        @NotNull UUID id,
        @NotNull String content
) {
}
