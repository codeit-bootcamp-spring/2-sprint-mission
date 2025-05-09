package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class WrongPasswordException extends UserException {
    public WrongPasswordException(String username) {
        super(
                Instant.now(),
                ErrorCode.WRONG_PASSWORD,
                Map.of("username", username)
        );
    }
}
