package com.sprint.mission.discodeit.Exception.NotFound;

public class UserStatusNotFoundException extends RuntimeException {
    public UserStatusNotFoundException(String message) {
        super(message);
    }
}
