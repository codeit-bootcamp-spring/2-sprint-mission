package com.sprint.mission.discodeit.domain.userstatus.exception;

import java.util.Map;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_USER_STATUS_NOT_FOUND;

public class UserStatusNotFoundException extends UserStatusException {

    public UserStatusNotFoundException(Map<String, Object> details) {
        super(ERROR_USER_STATUS_NOT_FOUND, details);
    }

}
