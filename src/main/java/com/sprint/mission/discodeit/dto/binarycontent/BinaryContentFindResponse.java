package com.sprint.mission.discodeit.dto.binarycontent;

import java.util.UUID;

public record BinaryContentFindResponse(
        UUID binaryId,
        String filePath,
        String fileName,
        String contentType,
        long fileSize
) {
}
