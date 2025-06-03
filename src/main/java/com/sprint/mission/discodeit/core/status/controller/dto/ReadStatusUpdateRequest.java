package com.sprint.mission.discodeit.core.status.controller.dto;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
