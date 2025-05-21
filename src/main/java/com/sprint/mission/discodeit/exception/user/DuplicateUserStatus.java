package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class DuplicateUserStatus extends UserException {
    public DuplicateUserStatus() {
        super(ErrorCode.DUPLICATE_USER_STATUS);
    }

    public DuplicateUserStatus(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_USER_STATUS, details);
    }
}
