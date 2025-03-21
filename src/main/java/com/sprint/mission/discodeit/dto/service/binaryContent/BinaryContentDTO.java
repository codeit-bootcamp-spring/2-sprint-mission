package com.sprint.mission.discodeit.dto.service.binaryContent;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
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
