package com.sprint.mission.discodeit.dto.service.binaryContent;

public record CreateBinaryContentParam(
        String filename,
        long size,
        String contentType,
        byte[] bytes
) {
}
