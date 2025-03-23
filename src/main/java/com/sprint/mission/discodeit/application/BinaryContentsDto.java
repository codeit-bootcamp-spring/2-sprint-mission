package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;

public record BinaryContentsDto(List<BinaryContentDto> binaryContents) {
    public static BinaryContentsDto fromEntity(List<BinaryContent> binaryContents) {
        List<BinaryContentDto> binaryContentDtos = binaryContents.stream()
                .map(BinaryContentDto::fromEntity)
                .toList();

        return new BinaryContentsDto(binaryContentDtos);
    }
}
