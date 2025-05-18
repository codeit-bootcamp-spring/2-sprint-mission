package com.sprint.mission.discodeit.message.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_MESSAGE_NOT_FOUND_BY_ID;

public class MessageNotFoundException extends MessageException {

    public MessageNotFoundException(Map<String, Object> details) {
        super(ERROR_MESSAGE_NOT_FOUND_BY_ID, details);
    }

}
