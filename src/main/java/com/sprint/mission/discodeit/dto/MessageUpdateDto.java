package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageUpdateDto(
        @NotNull UUID id,
        @NotNull String content
) {
}
