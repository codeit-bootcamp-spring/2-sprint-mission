package com.sprint.mission.discodeit.dto.service;

import lombok.Builder;
import java.time.Instant;
import java.util.UUID;


@Builder
public record UserDTO(
        UUID id,
        UUID profileId,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        Boolean isLogin
) {
}
