package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public record BinaryContent(
        UUID id,
        Instant createdAt,
        byte[] binaryImage
) {
}
