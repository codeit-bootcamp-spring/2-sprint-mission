package com.sprint.mission.discodeit.domain.userstatus.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class UserStatusException extends DiscodeitException {

    public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

}
