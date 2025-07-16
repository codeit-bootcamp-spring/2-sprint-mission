package com.sprint.mission.discodeit.core.read.dto.request;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt,
    Boolean newNotificationEnabled
) {

}
