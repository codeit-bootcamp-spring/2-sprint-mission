package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReadStatusUpdateDto(
        @NotNull UUID id,
        @NotNull UUID userId,
        @NotNull UUID channelId,
        @NotNull Boolean readStatus) {
}
