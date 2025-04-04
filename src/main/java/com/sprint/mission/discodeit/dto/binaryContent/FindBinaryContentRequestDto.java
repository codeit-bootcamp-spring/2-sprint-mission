package com.sprint.mission.discodeit.dto.binaryContent;

import java.time.Instant;

public record FindBinaryContentRequestDto(
    String fileName,
    String contentType,
    byte[] bytes,
    Instant createdAt
) {

}
