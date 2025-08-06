package com.sprint.mission.discodeit.event.sse;

import java.time.Instant;
import java.util.UUID;

public record SseChannelRefreshEvent(
    UUID receiverId,
    UUID channelId,
    Instant timestamp
) {

}