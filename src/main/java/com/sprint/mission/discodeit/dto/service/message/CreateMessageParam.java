package com.sprint.mission.discodeit.dto.service.message;

import java.util.List;
import java.util.UUID;


public record CreateMessageParam(
        String content,
        List<UUID> attachmentsId,
        UUID channelId,
        UUID authorId
) {
}
