package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class AuthInvalidCredentianls extends AuthException {

    public AuthInvalidCredentianls() {
        super(ErrorCode.AUTH_INVALID_CREDENTIALS);
    }

    public AuthInvalidCredentianls(Map<String, Object> details) {
        super(ErrorCode.AUTH_INVALID_CREDENTIALS, details);
    }
}
