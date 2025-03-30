package com.sprint.mission.discodeit.exception.handler.custom.message;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
