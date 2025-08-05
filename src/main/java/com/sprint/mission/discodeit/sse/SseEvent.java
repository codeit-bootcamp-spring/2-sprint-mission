package com.sprint.mission.discodeit.sse;

import java.util.Map;
import java.util.UUID;

public record SseEvent(
    UUID id,
    String name,
    Map<String, Object> data
) {

}
