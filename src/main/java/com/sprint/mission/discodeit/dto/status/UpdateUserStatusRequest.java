package com.sprint.mission.discodeit.dto.status;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusRequest(
        UUID userId,
        Instant lastAccessAt
) {}
