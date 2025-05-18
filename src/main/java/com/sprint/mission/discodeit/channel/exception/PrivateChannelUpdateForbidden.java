package com.sprint.mission.discodeit.channel.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_CHANNEL_PRIVATE_UPDATE_FORBIDDEN;

public class PrivateChannelUpdateForbidden extends ChannelException {

    public PrivateChannelUpdateForbidden(Map<String, Object> details) {
        super(ERROR_CHANNEL_PRIVATE_UPDATE_FORBIDDEN, details);
    }

}
