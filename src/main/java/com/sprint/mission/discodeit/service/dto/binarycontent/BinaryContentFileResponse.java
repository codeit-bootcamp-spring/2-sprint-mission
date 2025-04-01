package com.sprint.mission.discodeit.service.dto.binarycontent;

public record BinaryContentFileResponse(
        String contentType,
        byte[] fileBytes
) {
}
