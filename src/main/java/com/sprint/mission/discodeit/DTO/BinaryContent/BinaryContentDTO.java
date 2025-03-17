package com.sprint.mission.discodeit.DTO.BinaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record BinaryContentDTO(
        BinaryContent binaryContent,
        UUID binaryContentId,
        Instant createdAt
) {

    public static BinaryContentDTO create(BinaryContent binaryContent) {
        return BinaryContentDTO.builder()
                .binaryContent(binaryContent)
                .binaryContentId(binaryContent.getBinaryContentId())
                .createdAt(binaryContent.getCreatedAt()).build();
    }
}
