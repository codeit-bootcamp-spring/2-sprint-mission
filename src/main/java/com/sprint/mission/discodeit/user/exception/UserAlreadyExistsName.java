package com.sprint.mission.discodeit.user.exception;

import java.time.Instant;
import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_USER_ALREADY_EXISTS_NAME;

public class UserAlreadyExistsName extends UserException {

    public UserAlreadyExistsName(Map<String, Object> details) {
        super(Instant.now(), ERROR_USER_ALREADY_EXISTS_NAME, details);
    }

}
