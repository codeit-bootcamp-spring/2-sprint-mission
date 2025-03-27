package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.UUID;

public record ChannelDeleteResponse (
        UUID channelId,
        Instant deleteTime
) {
}
