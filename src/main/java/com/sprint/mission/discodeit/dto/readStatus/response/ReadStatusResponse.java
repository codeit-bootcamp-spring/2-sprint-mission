package com.sprint.mission.discodeit.dto.readStatus.response;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        Instant lastReadTime
) {}
