package com.sprint.mission.discodeit.auth.exception;

import com.sprint.mission.common.exception.DiscodeitException;
import com.sprint.mission.common.exception.ErrorCode;

import java.util.Map;

public class AuthException extends DiscodeitException {

    public AuthException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

}
