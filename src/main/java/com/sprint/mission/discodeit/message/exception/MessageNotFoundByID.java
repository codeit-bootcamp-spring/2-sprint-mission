package com.sprint.mission.discodeit.message.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_MESSAGE_NOT_FOUND_BY_ID;

public class MessageNotFoundByID extends MessageException {

    public MessageNotFoundByID(Map<String, Object> details) {
        super(ERROR_MESSAGE_NOT_FOUND_BY_ID, details);
    }

}
