package com.sprint.mission.discodeit.application.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentDto(UUID id, String path) {
    public static BinaryContentDto fromEntity(BinaryContent binaryContent) {
        return new BinaryContentDto(binaryContent.getId(), binaryContent.getPath());
    }
}
