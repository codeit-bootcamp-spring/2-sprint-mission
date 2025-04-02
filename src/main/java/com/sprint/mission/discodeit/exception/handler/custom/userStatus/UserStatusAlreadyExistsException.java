package com.sprint.mission.discodeit.exception.handler.custom.userStatus;

public class UserStatusAlreadyExistsException extends RuntimeException {
    public UserStatusAlreadyExistsException(String message) {
        super(message);
    }
}
