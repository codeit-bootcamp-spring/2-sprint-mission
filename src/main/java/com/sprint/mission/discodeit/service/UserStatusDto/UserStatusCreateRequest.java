package com.sprint.mission.discodeit.service.UserStatusDto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId,
        Instant status) {
}
