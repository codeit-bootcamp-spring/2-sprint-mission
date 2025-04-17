package com.sprint.mission.discodeit.dto.service.user.userstatus;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
