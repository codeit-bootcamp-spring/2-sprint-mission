package com.sprint.mission.discodeit.dto.file;

import java.util.UUID;

public record CreateBinaryContentRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
