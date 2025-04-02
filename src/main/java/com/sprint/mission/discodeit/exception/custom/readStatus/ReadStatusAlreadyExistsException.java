package com.sprint.mission.discodeit.exception.custom.readStatus;

public class ReadStatusAlreadyExistsException extends IllegalArgumentException {
    public ReadStatusAlreadyExistsException(String message) {
        super(message);
    }
}
