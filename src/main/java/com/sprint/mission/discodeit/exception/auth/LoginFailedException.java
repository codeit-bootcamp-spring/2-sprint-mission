package com.sprint.mission.discodeit.exception.auth;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {
        super(message);
    }
    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
