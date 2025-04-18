package com.sprint.mission.discodeit.dto.binaryContent;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        long size,
        String contentType
) {
}
