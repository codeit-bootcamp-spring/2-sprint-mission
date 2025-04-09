package com.sprint.mission.discodeit.dto.request;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserStatusCreateRequest(
    UUID userId,
    OffsetDateTime lastActiveAt
) {

}
