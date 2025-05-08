package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_USER, details);
    }

    public static UserAlreadyExistsException forUsername(String username) {
        return new UserAlreadyExistsException(Map.of("username", username));
    }

    public static UserAlreadyExistsException forEmail(String email) {
        return new UserAlreadyExistsException(Map.of("email", email));
    }
}
