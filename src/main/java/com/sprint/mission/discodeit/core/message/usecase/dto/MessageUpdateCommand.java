package com.sprint.mission.discodeit.core.message.usecase.dto;

import java.util.UUID;

public record MessageUpdateCommand(
    UUID messageId,
    String newText
) {

}
