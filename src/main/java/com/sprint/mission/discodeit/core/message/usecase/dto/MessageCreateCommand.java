package com.sprint.mission.discodeit.core.message.usecase.dto;

import java.util.UUID;

public record MessageCreateCommand(
    UUID authorId,
    UUID channelId,
    String content
) {

}
