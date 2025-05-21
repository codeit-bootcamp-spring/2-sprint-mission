package com.sprint.mission.discodeit.domain.channel.exception;

import java.util.Map;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_CHANNEL_PRIVATE_UPDATE_FORBIDDEN;

public class PrivateChannelUpdateForbiddenException extends ChannelException {

    public PrivateChannelUpdateForbiddenException(Map<String, Object> details) {
        super(ERROR_CHANNEL_PRIVATE_UPDATE_FORBIDDEN, details);
    }

}
