package com.sprint.mission.discodeit.service.dto.binarycontentdto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        Long size,
        String contentType
) {

}
