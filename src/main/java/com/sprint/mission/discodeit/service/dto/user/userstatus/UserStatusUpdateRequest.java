package com.sprint.mission.discodeit.service.dto.user.userstatus;

import java.time.Instant;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {
}
