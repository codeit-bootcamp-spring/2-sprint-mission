package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidPasswordException extends AuthException {
    public InvalidPasswordException(String password) {
        super(ErrorCode.INVALID_PASSWORD, Map.of("password", password));
    }
}
