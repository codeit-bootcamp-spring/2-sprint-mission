package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class ChannelNotFound extends ChannelException {
    public ChannelNotFound() {
        super(ErrorCode.CHANNEL_NOT_FOUND);
    }

    public ChannelNotFound(Map<String, Object> details) {
        super(ErrorCode.CHANNEL_NOT_FOUND, details);
    }
}
