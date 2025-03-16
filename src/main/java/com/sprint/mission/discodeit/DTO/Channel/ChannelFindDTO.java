package com.sprint.mission.discodeit.DTO.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindDTO(
        UUID channelId,
        String name,
        List<UUID> usersId,
        Instant lastlyMessageAt
) {
}
