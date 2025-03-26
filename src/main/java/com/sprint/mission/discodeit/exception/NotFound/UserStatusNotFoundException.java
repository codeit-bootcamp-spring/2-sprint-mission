package com.sprint.mission.discodeit.exception.NotFound;

public class UserStatusNotFoundException extends RuntimeException {
    public UserStatusNotFoundException(String message) {
        super(message);
    }
}
