package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusException extends DiscodeitException {
    protected UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public UserStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
