package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class UserAlreadyExistException extends UserException {
    public UserAlreadyExistException(String username) {
        super(
                Instant.now(),
                ErrorCode.USER_ALREADY_EXISTS,
                Map.of("username", username)
        );
    }
}
