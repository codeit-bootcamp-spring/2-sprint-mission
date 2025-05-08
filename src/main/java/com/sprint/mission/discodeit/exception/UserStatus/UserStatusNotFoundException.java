package com.sprint.mission.discodeit.exception.UserStatus;

import com.sprint.mission.discodeit.constant.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.util.Map;

public class UserStatusNotFoundException extends DiscodeitException {

    public UserStatusNotFoundException(Map<String, Object> details) {
        super(ErrorCode.USER_STATUS_NOT_FOUND, details);
    }

    public static UserStatusNotFoundException forUserId(String userId) {
        return new UserStatusNotFoundException(Map.of("userId", userId));
    }
}
