package com.sprint.mission.discodeit.exception.NotFound;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
