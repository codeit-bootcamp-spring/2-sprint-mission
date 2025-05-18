package com.sprint.mission.discodeit.user.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_USER_NOT_FOUND_BY_EMAIL;

public class UserNotFoundByEmail extends UserException {

    public UserNotFoundByEmail(Map<String, Object> details) {
        super(ERROR_USER_NOT_FOUND_BY_EMAIL, details);
    }

}
