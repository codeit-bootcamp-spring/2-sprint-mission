package com.sprint.mission.discodeit.DTO.RequestToService;

public record BinaryContentCreateDTO(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
