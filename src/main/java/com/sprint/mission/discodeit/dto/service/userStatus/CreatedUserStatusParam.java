package com.sprint.mission.discodeit.dto.service.userStatus;

import java.time.Instant;
import java.util.UUID;

public record CreatedUserStatusParam(
    UUID userId,
    Instant lastActiveAt
) {

}
