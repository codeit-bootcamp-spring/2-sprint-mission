package com.sprint.mission.discodeit.exception.Valid;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
