package com.sprint.mission.discodeit.user.exception;

import java.time.Instant;
import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_USER_NOT_FOUND_BY_ID;

public class UserNotFoundByID extends UserException {

    public UserNotFoundByID(Map<String, Object> details) {
        super(Instant.now(), ERROR_USER_NOT_FOUND_BY_ID, details);
    }

}
