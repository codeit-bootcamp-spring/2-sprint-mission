package com.sprint.mission.discodeit.adapter.inbound.status.request;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
