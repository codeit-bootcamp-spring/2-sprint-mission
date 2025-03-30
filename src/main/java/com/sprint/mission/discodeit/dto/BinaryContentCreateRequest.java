package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContentType;

public record BinaryContentCreateRequest(
        BinaryContentType type,
        byte[] content
) {
}
