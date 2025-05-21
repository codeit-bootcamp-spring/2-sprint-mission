package com.sprint.mission.discodeit.domain.user.exception;

import java.util.Map;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_USER_ALREADY_EXISTS;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(Map<String, Object> details) {
        super(ERROR_USER_ALREADY_EXISTS, details);
    }

}
