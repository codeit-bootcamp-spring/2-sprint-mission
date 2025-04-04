package com.sprint.mission.discodeit.dto.readStatus.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReadStatusCreatRequest(
        @NotNull UUID userId,
        @NotNull UUID channelId
) {
}
