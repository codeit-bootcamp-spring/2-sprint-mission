package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentCreateDto(
        String fileName,
        Long size,
        String contentType,
        byte[] bytesImage
) {
}
