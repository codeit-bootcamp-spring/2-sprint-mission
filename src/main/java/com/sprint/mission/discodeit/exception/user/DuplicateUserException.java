package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateUserException extends UserException {

    public DuplicateUserException() {
        super(ErrorCode.DUPLICATE_USER);
    }

    public DuplicateUserException(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_USER, details);
    }
}
