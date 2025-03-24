package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record SaveMessageParamDto(
        UUID channelId,
        UUID userId,
        String content,
        List<UUID> attachmentList
) {
}
