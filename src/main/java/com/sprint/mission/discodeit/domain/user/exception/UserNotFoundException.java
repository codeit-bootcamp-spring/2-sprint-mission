package com.sprint.mission.discodeit.domain.user.exception;

import java.util.Map;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_USER_NOT_FOUND;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(Map<String, Object> details) {
        super(ERROR_USER_NOT_FOUND, details);
    }

}
