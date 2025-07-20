package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record BinaryContentCreateEvent(
    UUID binaryContentId,
    byte[] bytes,
    String fileName,
    String contentType,
    String requestId,
    UUID userId
) {

}

