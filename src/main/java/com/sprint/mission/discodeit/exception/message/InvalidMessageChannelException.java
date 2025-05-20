package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidMessageChannelException extends MessageException {

    public InvalidMessageChannelException() {
        super(ErrorCode.INVALID_MESSAGE_CHANNEL);
    }

    public InvalidMessageChannelException(Map<String, Object> details) {
        super(ErrorCode.INVALID_MESSAGE_CHANNEL, details);
    }
}
