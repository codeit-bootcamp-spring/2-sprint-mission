package com.sprint.mission.discodeit.exception.Valid;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
