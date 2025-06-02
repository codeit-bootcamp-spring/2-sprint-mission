package com.sprint.mission.discodeit.dto.data;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}
