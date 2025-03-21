package com.sprint.mission.discodeit.dto.service.userStatus;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserStatusDTO(
        UUID id,
        UUID userId,
        Instant cratedAt,
        Instant updatedAt
) {
}
