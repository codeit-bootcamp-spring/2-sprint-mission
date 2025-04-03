package com.sprint.mission.discodeit.controller.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserStatusCreateRequest(
    UUID userId,
    OffsetDateTime lastActiveAt
) {

}
