package com.sprint.mission.discodeit.dto.create;

public record BinaryContentCreateRequestDTO(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
