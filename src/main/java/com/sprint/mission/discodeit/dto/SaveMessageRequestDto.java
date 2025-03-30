package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record SaveMessageRequestDto(
        UUID channelId,
        UUID userId,
        String content
) {
}
