package com.sprint.mission.discodeit.domain.message.exception;

import java.util.Map;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_MESSAGE_NOT_FOUND;

public class MessageNotFoundException extends MessageException {

    public MessageNotFoundException(Map<String, Object> details) {
        super(ERROR_MESSAGE_NOT_FOUND, details);
    }

}
