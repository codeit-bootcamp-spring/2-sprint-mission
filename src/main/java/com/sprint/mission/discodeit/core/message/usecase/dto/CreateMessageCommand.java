package com.sprint.mission.discodeit.core.message.usecase.dto;

import java.util.UUID;

public record CreateMessageCommand(
    UUID authorId,
    UUID channelId,
    String content
) {

}
