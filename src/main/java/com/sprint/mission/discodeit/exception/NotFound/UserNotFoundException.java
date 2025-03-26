package com.sprint.mission.discodeit.exception.NotFound;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
