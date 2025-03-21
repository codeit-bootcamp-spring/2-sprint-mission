package com.sprint.mission.discodeit.service.dto.binarycontent;

public record BinaryContentCreateRequest(
        String fileName,
        String type,
        byte[] bytes
) {
}
