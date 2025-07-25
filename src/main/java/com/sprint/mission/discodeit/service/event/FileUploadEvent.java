package com.sprint.mission.discodeit.service.event;

import java.util.UUID;

public record FileUploadEvent(
    UUID binaryContentId,
    byte[] bytes,
    UUID userId
) {
}