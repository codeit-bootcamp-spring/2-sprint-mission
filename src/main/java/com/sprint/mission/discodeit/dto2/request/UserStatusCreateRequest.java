package com.sprint.mission.discodeit.dto2.request;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId,
        Instant lastAccessedAt
) {}
