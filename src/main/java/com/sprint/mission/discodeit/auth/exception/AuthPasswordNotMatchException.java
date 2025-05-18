package com.sprint.mission.discodeit.auth.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_AUTH_PASSWORD_NOT_MATCH;

public class AuthPasswordNotMatchException extends AuthException {

    public AuthPasswordNotMatchException(Map<String, Object> details) {
        super(ERROR_AUTH_PASSWORD_NOT_MATCH, details);
    }

}
