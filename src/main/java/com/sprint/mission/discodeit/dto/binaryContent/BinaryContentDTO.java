package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentDTO(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
