package com.sprint.mission.discodeit.exception.custom.user;

public class UserNameAlreadyExistsException extends IllegalArgumentException {
    public UserNameAlreadyExistsException(String message) {
        super(message);
    }
}
