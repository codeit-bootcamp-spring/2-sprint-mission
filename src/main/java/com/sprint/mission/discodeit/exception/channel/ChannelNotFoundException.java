package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

    public ChannelNotFoundException(Map<String, Object> details) {
        super(ErrorCode.CHANNEL_NOT_FOUND, details);
    }

    public static ChannelNotFoundException forId(String channelId) {
        return new ChannelNotFoundException(Map.of("channelId", channelId));
    }
}
