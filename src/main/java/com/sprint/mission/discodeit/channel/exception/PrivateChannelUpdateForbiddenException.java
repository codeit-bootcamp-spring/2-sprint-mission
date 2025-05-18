package com.sprint.mission.discodeit.channel.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_CHANNEL_PRIVATE_UPDATE_FORBIDDEN;

public class PrivateChannelUpdateForbiddenException extends ChannelException {

    public PrivateChannelUpdateForbiddenException(Map<String, Object> details) {
        super(ERROR_CHANNEL_PRIVATE_UPDATE_FORBIDDEN, details);
    }

}
