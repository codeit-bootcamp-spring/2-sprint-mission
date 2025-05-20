package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class PrivateChannelModifyException extends ChannelException {

    public PrivateChannelModifyException(Map<String, Object> details) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
    }

    public static PrivateChannelModifyException forId(String channelId) {
        return new PrivateChannelModifyException(Map.of("channelId", channelId));
    }
}
