package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record BinaryContentUploadSuccessEvent(
    UUID binaryContentId,
    String requestId
) {

}
