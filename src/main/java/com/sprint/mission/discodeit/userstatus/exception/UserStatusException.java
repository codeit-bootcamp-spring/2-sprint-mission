package com.sprint.mission.discodeit.userstatus.exception;

import com.sprint.mission.common.exception.DiscodeitException;
import com.sprint.mission.common.exception.ErrorCode;

import java.util.Map;

public class UserStatusException extends DiscodeitException {

    public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

}
