package com.sprint.mission.discodeit.exception.user;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
}
