package com.sprint.mission.discodeit.exception.handler.custom.user;

public class UserNameAlreadyExistsException extends RuntimeException {
    public UserNameAlreadyExistsException(String message) {
        super(message);
    }
}
