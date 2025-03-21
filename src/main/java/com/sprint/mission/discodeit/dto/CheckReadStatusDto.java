package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record CheckReadStatusDto(
        UUID ChannelUUID,
        String channelName,
        Instant lastMessageTime
) {
}
