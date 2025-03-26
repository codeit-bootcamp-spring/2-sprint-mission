package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PrivateChannelReadResponse(
        UUID channelId,
        Instant lastMessageTime,
        List<UUID> userIds
) implements ChannelReadResponse {

}
