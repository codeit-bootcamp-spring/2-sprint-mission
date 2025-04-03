package com.sprint.mission.discodeit.dto.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateReadStatusDto(
        UUID id,
        Instant lastReadAt
) {}
