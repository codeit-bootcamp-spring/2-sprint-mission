package com.sprint.mission.discodeit.dto.requestToService;

public record BinaryContentCreateDTO(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
