package com.sprint.mission.discodeit.core.message.usecase.crud.dto;

import java.util.UUID;

public record MessageCreateCommand(
    UUID userId,
    UUID channelId,
    String text
) {

}
