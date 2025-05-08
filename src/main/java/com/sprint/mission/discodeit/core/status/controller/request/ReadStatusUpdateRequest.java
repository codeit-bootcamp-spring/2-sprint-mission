package com.sprint.mission.discodeit.core.status.controller.request;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
