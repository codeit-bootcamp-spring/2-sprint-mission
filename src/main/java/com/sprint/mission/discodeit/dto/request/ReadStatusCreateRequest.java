package com.sprint.mission.discodeit.dto.request;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID getChannelId,
    OffsetDateTime lastReadAt
) {

}
