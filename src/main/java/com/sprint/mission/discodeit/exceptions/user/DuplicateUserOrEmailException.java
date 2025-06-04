package com.sprint.mission.discodeit.exceptions.user;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class DuplicateUserOrEmailException extends UserException {
    public DuplicateUserOrEmailException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(timestamp, errorCode, details);
    }

    public DuplicateUserOrEmailException(Map<String, Object> details) {
        this(Instant.now(), ErrorCode.DUPLICATE_USER_OR_EMAIL, details);
    }
}