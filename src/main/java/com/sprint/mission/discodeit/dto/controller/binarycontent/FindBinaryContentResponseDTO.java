package com.sprint.mission.discodeit.dto.controller.binarycontent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FindBinaryContentResponseDTO(
    UUID id,
    String filename,
    long size,
    String contentType,
    byte[] bytes
) {

}
