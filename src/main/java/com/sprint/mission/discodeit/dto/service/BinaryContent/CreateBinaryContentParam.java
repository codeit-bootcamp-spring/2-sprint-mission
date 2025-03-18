package com.sprint.mission.discodeit.dto.service.BinaryContent;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateBinaryContentParam(
        String filename,
        byte[] content
) {
}
