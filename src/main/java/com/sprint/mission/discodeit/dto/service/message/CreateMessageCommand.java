package com.sprint.mission.discodeit.dto.service.message;

import java.util.UUID;

public record CreateMessageCommand(
    String content,
    UUID channelId,
    UUID authorId
) {

}
