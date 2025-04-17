package com.sprint.mission.discodeit.dto.service.binarycontent;

import java.util.UUID;

public record FindBinaryContentResult(
    UUID id,
    String filename,
    long size,
    String contentType,
    byte[] bytes
) {

}
