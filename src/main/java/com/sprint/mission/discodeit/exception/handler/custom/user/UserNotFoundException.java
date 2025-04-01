package com.sprint.mission.discodeit.exception.handler.custom.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
