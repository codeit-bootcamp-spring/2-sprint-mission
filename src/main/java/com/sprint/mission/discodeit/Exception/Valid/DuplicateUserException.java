package com.sprint.mission.discodeit.Exception.Valid;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
