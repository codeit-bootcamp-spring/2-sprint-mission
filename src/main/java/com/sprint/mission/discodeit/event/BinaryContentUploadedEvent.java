package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record BinaryContentUploadedEvent(
    UUID contentId,
    byte[] bytes
) {

}
