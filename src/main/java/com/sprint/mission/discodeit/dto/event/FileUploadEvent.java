package com.sprint.mission.discodeit.dto.event;

import java.util.UUID;

public record FileUploadEvent(
    UUID id,
    byte[] bytes
) {

}
