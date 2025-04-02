package com.sprint.mission.discodeit.exception.custom.user;

public class UserEmailAlreadyExistsException extends IllegalArgumentException {
    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}
