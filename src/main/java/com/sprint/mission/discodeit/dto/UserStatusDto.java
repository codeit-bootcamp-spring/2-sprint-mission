package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.domain.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

}
