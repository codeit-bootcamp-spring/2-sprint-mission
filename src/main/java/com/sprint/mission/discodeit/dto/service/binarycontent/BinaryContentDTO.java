package com.sprint.mission.discodeit.dto.service.binarycontent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDTO(
        UUID id,
        Instant createdAt,
        String filename,
        long size,
        String contentType,
        byte[] bytes
) {
}
