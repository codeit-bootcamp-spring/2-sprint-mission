package com.sprint.mission.discodeit.message.exception;

import com.sprint.mission.common.exception.DiscodeitException;
import com.sprint.mission.common.exception.ErrorCode;

import java.util.Map;

public class MessageException extends DiscodeitException {

    public MessageException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

}
