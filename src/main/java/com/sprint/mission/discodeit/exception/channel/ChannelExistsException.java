package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelExistsException extends ChannelException {
    public ChannelExistsException(String channelName) {
        super(ErrorCode.CHANNEL_ALREADY_EXISTS, Map.of("channelName", channelName));
    }
}
