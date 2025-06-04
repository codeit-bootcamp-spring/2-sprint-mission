package com.sprint.mission.discodeit.exceptions.user;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(timestamp, errorCode, details);
    }

    public UserNotFoundException(Map<String, Object> details) {
        this(Instant.now(), ErrorCode.USER_NOT_FOUND, details);
    }
}