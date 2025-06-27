package com.sprint.mission.discodeit.core.user.dto.request;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(
    UUID userStatusId,
    Instant newLastActiveAt
) {

}
