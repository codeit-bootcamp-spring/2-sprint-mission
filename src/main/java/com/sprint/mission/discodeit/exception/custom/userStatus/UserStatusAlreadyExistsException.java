package com.sprint.mission.discodeit.exception.custom.userStatus;

public class UserStatusAlreadyExistsException extends IllegalArgumentException {
    public UserStatusAlreadyExistsException(String message) {
        super(message);
    }
}
