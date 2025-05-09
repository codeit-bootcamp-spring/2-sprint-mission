package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class DuplicateUserUserName extends UserException {
    public DuplicateUserUserName() {
        super(ErrorCode.DUPLICATE_USER_USERNAME);
    }

    public DuplicateUserUserName(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_USER_USERNAME, details);
    }
}
