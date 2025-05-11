package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {

    public PrivateChannelUpdateException() {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE_DENIED);
    }

    public PrivateChannelUpdateException(Map<String, Object> details) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE_DENIED, details);
    }
}
