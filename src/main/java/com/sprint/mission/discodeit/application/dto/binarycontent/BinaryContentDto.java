package com.sprint.mission.discodeit.application.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.nio.file.Path;
import java.util.UUID;

public record BinaryContentDto(UUID id, Path path) {
    public static BinaryContentDto fromEntity(BinaryContent binaryContent) {
        return new BinaryContentDto(binaryContent.getProfileId(), binaryContent.getPath());
    }
}
