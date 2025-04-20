package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BinaryContentDto(
     UUID id,
     String fileName,
     long size,
     String contentType,
     byte[] bytes
) {
}
