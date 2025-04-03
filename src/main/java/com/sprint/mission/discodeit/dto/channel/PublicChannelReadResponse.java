package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.UUID;

public record PublicChannelReadResponse(
        UUID channelId,
        Instant lastMessageTime,
        String channelName
) implements ChannelReadResponse {
}
