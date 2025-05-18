package com.sprint.mission.discodeit.user.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_USER_ALREADY_EXISTS_NAME;

public class UserAlreadyExistsName extends UserException {

    public UserAlreadyExistsName(Map<String, Object> details) {
        super( ERROR_USER_ALREADY_EXISTS_NAME, details);
    }

}
