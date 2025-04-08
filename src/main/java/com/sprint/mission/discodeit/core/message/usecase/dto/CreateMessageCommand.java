package com.sprint.mission.discodeit.core.message.usecase.dto;

import java.util.UUID;

public record CreateMessageCommand(
    UUID userId,
    UUID channelId,
    String text
) {

}
