package com.sprint.mission.discodeit.exception.Message;

import com.sprint.mission.discodeit.exception.Basic.DiscodeitException;
import com.sprint.mission.discodeit.exception.Basic.ErrorCode;

import java.util.Map;

public class MessageException extends DiscodeitException {
    public MessageException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
