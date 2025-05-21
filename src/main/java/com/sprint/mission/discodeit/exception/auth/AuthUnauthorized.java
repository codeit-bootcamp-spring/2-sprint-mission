package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class AuthUnauthorized extends AuthException {
    public AuthUnauthorized(ErrorCode errorCode) {
        super(ErrorCode.AUTH_UNAUTHORIZED);
    }

    public AuthUnauthorized(Map<String, Object> details) {
        super(ErrorCode.AUTH_UNAUTHORIZED, details);
    }
}
