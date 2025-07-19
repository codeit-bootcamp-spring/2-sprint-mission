package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record BinaryContentUploadFailureEvent(
    UUID binaryContentId,
    String requestId,
    String taskName,
    String failureReason
) {

}
