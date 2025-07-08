package com.sprint.mission.discodeit.core.read.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull UUID userId,
    @NotNull UUID channelId,
    Instant lastReadAt
) {

}
