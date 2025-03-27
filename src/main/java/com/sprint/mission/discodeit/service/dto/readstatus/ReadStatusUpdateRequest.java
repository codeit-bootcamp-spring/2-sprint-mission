package com.sprint.mission.discodeit.service.dto.readstatus;

import java.time.Instant;

public record ReadStatusUpdateRequest(
        Instant lastReadAt
) {
}
