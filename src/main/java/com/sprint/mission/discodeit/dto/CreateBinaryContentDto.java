package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record CreateBinaryContentDto(
        String fileName,
        byte[] binaryContent
) {
}
