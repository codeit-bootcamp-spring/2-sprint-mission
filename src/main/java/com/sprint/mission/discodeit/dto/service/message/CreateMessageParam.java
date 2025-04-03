package com.sprint.mission.discodeit.dto.service.message;

import java.util.List;
import java.util.UUID;


public record CreateMessageParam(
        String content,
        UUID channelId,
        UUID authorId
) {
}
