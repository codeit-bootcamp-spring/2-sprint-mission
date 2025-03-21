package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindDTO(
        UUID channelId,
        String name,
        List<UUID> userIdList,
        Instant lastMessageAt
) {
}
