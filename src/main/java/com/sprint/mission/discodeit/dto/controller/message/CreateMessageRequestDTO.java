package com.sprint.mission.discodeit.dto.controller.message;

import java.util.UUID;

public record CreateMessageRequestDTO(
        String content,
        UUID channelId,
        UUID authorId
) {
}
