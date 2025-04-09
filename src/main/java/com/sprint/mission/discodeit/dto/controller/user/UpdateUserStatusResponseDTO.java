package com.sprint.mission.discodeit.dto.controller.user;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusResponseDTO(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

}
