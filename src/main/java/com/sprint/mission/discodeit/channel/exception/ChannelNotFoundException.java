package com.sprint.mission.discodeit.channel.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_CHANNEL_NOT_FOUND;

public class ChannelNotFoundException extends ChannelException {

    public ChannelNotFoundException(Map<String, Object> details) {
        super(ERROR_CHANNEL_NOT_FOUND, details);
    }

}
