package com.sprint.mission.discodeit.Exception.NotFound;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
