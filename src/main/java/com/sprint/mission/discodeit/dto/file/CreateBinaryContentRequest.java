package com.sprint.mission.discodeit.dto.file;

import java.util.UUID;

public record CreateBinaryContentRequest(
        UUID userId,
        UUID messageId,
        String fileName,
        String filePath
) {}
