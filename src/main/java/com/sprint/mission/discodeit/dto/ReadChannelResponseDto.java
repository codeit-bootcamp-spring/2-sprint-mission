package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReadChannelResponseDto(
        UUID channelKey,
        ChannelType type,
        String channelName,
        String introduction,
        Instant lastMessageAt,
        List<UUID> userKey
) {
}
