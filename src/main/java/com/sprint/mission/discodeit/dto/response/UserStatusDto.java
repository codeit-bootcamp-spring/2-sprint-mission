package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserStatusDto(
        UUID id,
        UUID userId,
        Instant lastActiveAt
) {
}
