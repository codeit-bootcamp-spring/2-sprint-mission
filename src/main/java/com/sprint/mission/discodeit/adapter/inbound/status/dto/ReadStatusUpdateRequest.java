package com.sprint.mission.discodeit.adapter.inbound.status.dto;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
