package com.sprint.mission.discodeit.exceptions.Auth;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class AuthFailException extends AuthException {
    public AuthFailException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(timestamp, errorCode, details);
    }
}
