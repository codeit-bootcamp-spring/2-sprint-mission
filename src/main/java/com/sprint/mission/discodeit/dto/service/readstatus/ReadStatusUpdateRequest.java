package com.sprint.mission.discodeit.dto.service.readstatus;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
