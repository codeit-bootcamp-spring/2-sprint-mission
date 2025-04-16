package com.sprint.mission.discodeit.service.dto.binarycontentdto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        Long size,
        String contentType
) {

}
