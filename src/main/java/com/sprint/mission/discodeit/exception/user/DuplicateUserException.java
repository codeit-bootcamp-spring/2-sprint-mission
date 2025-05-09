package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class DuplicateUserException extends UserException {
    public DuplicateUserException(String email) {
        super(ErrorCode.DUPLICATE_USER, Map.of("email", email));
    }

    public DuplicateUserException(String username, String type) {
        super(ErrorCode.DUPLICATE_USER, Map.of("username", username, "type", type));
    }
}
