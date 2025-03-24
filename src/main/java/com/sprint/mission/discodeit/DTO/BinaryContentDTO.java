package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;

public record BinaryContentDTO(
        BinaryContentType type,
        byte[] content
) {
    public static BinaryContentDTO toDto (BinaryContent binaryContent) {
        return new BinaryContentDTO(binaryContent.getType(), binaryContent.getContent());
    }

    public BinaryContent toEntity(BinaryContentDTO dto) {
        return new BinaryContent(dto.type,dto.content);
    }
}
