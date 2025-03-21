package com.sprint.mission.discodeit.dto.request;

public record CreateBinaryContentRequestDTO(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
