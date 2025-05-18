package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.common.exception.DiscodeitException;
import com.sprint.mission.common.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class UserException extends DiscodeitException {

    public UserException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(timestamp, errorCode, details);
    }

}
