package com.sprint.mission.discodeit.event.sse;

import java.time.Instant;
import java.util.UUID;

public record SseUserRefreshEvent(
    UUID receiverId,
    UUID targetUserId,
    Instant timestamp
) {

}