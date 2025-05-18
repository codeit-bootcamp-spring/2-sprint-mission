package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.common.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_USER_NOT_FOUND_BY_NAME;

public class UserNotFoundByName extends UserException {

    public UserNotFoundByName(Map<String, Object> details) {
        super(Instant.now(), ERROR_USER_NOT_FOUND_BY_NAME, details);
    }

}
