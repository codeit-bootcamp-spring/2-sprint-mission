package com.sprint.mission.discodeit.application.dto.message;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreationRequest(@NotNull String context, @NotNull UUID chanelId, @NotNull UUID userId) {
}
