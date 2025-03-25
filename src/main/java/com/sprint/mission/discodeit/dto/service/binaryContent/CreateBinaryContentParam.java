package com.sprint.mission.discodeit.dto.service.binaryContent;

public record CreateBinaryContentParam(
        String filename,
        String path,
        long size,
        String type,
        byte[] bytes
) {
}
