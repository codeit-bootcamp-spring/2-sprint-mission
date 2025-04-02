package com.sprint.mission.discodeit.core.message.usecase.dto;

import java.util.UUID;

public record UpdateMessageCommand(
    UUID messageId,
    String newText
) {

}
