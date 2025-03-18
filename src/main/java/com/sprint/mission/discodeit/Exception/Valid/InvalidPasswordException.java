package com.sprint.mission.discodeit.Exception.Valid;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
