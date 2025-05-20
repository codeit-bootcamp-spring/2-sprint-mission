package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class UserStatusException extends DiscodeitException {

    public UserStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
