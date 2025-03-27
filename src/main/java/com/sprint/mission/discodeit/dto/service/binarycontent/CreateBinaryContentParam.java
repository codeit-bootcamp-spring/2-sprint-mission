package com.sprint.mission.discodeit.dto.service.binarycontent;

public record CreateBinaryContentParam(
        String filename,
        long size,
        String contentType,
        byte[] bytes
) {
}
