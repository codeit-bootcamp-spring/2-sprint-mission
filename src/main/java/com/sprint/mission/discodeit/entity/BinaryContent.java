package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public record BinaryContent(
        UUID id,
        Instant createTime,
        UUID referenceId,
        String fileName,
        String fileType,
        String filePath
) {
    public BinaryContent(UUID referenceId, String filePath) {
        this(UUID.randomUUID(), Instant.now(), referenceId, extractFileName(filePath), determineFileType(filePath), filePath);
    }

    public static String extractFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    public static String determineFileType(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }
}
