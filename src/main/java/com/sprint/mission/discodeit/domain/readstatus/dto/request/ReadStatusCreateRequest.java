package com.sprint.mission.discodeit.domain.readstatus.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReadStatusCreateRequest(
        @NotNull UUID userId,
        @NotNull UUID channelId
) {
}
