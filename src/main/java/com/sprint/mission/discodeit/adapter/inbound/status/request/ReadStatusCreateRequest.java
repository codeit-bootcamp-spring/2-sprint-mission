package com.sprint.mission.discodeit.adapter.inbound.status.request;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
