package com.sprint.mission.discodeit.domain.userstatus.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
        @NotNull
        UUID userId,
        @NotNull
        Instant lastActiveAt
) {
}
