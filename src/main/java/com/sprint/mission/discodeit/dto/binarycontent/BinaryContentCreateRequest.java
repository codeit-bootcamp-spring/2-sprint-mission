package com.sprint.mission.discodeit.dto.binarycontent;

import java.util.UUID;

public record BinaryContentCreateRequest(
        UUID fileId,
        String filePath,
        String fileName,
        String fileType,
        long fileSize
) {
}
