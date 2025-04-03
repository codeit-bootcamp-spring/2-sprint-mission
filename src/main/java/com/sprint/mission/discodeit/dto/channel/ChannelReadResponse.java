package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.UUID;

public interface ChannelReadResponse {
    UUID channelId();
    Instant lastMessageTime();
}
