package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class PrivateChannelUpdateException extends ChannelException {
    public PrivateChannelUpdateException(UUID channelId) {
        super(
                Instant.now(),
                ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED,
                Map.of("channelId", channelId)
        );
    }
}
