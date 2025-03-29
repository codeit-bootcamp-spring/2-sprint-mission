package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;

public record BinaryContentDTO(
        BinaryContentType type,
        byte[] content
) {

    public BinaryContent toEntity(BinaryContentDTO dto) {
        return new BinaryContent(dto.type,dto.content);
    }
}
