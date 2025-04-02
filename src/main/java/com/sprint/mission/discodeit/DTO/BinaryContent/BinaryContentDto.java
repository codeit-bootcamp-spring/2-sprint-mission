package com.sprint.mission.discodeit.DTO.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        String contentType,
        byte[] bytes,
        Instant createdAt
) {}
