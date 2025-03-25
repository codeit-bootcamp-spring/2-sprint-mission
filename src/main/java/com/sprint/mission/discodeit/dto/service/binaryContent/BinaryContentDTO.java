package com.sprint.mission.discodeit.dto.service.binaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDTO(
        UUID id,
        Instant createdAt,
        String filename,
        String path,
        long size,
        String type,
        byte[] bytes
) {
}
