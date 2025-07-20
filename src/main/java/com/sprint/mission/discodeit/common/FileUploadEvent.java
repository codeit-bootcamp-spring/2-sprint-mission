package com.sprint.mission.discodeit.common;

import java.util.UUID;

public record FileUploadEvent(
        UUID binaryContentId,
        byte[] fileBytes,
        String fileName
) {
} 