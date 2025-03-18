package com.sprint.mission.discodeit.dto.service.BinaryContent;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record BinaryContentDTO(
        UUID id,
        Instant createdAt,
        String filename,
        byte[] content
) {
}
