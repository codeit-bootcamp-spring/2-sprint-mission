package com.sprint.mission.discodeit.dto.service.binaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDTO(
        UUID id,
        Instant createdAt,
        String filename,
        long size,
        String ContentType,
        byte[] bytes
) {
}
