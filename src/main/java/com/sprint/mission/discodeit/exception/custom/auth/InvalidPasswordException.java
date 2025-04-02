package com.sprint.mission.discodeit.exception.custom.auth;

import java.util.NoSuchElementException;

public class InvalidPasswordException extends NoSuchElementException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
