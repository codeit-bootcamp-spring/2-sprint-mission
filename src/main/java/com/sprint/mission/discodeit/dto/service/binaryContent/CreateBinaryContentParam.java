package com.sprint.mission.discodeit.dto.service.binaryContent;

import lombok.Builder;

@Builder
public record CreateBinaryContentParam(
        String filename,
        String path,
        long size,
        String type,
        byte[] bytes
) {
}
