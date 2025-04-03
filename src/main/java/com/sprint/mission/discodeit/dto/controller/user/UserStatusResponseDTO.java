package com.sprint.mission.discodeit.dto.controller.user;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDTO(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

}
