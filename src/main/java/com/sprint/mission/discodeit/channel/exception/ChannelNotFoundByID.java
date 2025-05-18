package com.sprint.mission.discodeit.channel.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_CHANNEL_NOT_FOUND_BY_ID;

public class ChannelNotFoundByID extends ChannelException {

    public ChannelNotFoundByID(Map<String, Object> details) {
        super(ERROR_CHANNEL_NOT_FOUND_BY_ID, details);
    }

}
