package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;


public class EmailAlreadyExistException extends UserException {
    public EmailAlreadyExistException(String email) {
        super(
                Instant.now(),
                ErrorCode.EMAIL_ALREADY_EXISTS,
                Map.of("email", email)
        );
    }
}
