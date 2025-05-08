package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(Map<String, Object> details) {
        super(ErrorCode.USER_NOT_FOUND, details);
    }

    public static UserNotFoundException forId(String userId) {
        return new UserNotFoundException(Map.of("userId", userId));
    }

    public static UserNotFoundException forUsername(String username) {
        return new UserNotFoundException(Map.of("username", username));
    }
}
