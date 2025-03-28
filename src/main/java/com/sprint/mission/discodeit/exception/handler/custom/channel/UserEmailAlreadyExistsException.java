package com.sprint.mission.discodeit.exception.handler.custom.channel;

public class UserEmailAlreadyExistsException extends RuntimeException {
    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}
