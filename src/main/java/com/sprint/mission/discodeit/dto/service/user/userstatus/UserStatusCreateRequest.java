package com.sprint.mission.discodeit.dto.service.user.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    UUID userId,
    Instant lastActiveAt
) {

}
