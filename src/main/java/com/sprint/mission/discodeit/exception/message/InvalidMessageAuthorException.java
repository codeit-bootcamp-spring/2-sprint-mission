package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidMessageAuthorException extends MessageException {

    public InvalidMessageAuthorException() {
        super(ErrorCode.INVALID_MESSAGE_AUTHOR);
    }

    public InvalidMessageAuthorException(Map<String, Object> details) {
        super(ErrorCode.INVALID_MESSAGE_AUTHOR, details);
    }
}
