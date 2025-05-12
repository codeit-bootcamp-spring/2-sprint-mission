package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class PrivateChannelUpdateNotSupportedException extends ChannelException {
    public PrivateChannelUpdateNotSupportedException(UUID channelId) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_SUPPORTED, Map.of("channelId", channelId));
    }
}
