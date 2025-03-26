package com.sprint.mission.discodeit.dto.create;

public record CreateBinaryContentRequestDTO(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
