package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto (
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) { }
