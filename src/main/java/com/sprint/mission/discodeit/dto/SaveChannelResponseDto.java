package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.constant.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record SaveChannelResponseDto(
        UUID channelUUID,
        String channelName,
        ChannelType channelType,
        Instant createdAt
) {
}
