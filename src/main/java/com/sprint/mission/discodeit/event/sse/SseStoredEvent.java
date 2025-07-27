package com.sprint.mission.discodeit.event.sse;

import java.io.Serializable;
import java.time.Instant;

public record SseStoredEvent(
    String eventId,
    String eventType,
    Instant timestamp,
    String data
) {

}