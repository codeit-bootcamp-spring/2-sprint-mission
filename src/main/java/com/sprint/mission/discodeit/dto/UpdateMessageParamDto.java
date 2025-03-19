package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateMessageParamDto(
    UUID messageUUID,
    String content
) {
}
