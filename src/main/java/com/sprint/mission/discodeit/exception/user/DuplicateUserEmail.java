package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class DuplicateUserEmail extends UserException {
    public DuplicateUserEmail() {
        super(ErrorCode.DUPLICATE_USER_EMAIL);
    }

    public DuplicateUserEmail(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_USER_EMAIL, details);
    }
}
