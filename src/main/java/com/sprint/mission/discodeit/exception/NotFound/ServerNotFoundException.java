package com.sprint.mission.discodeit.exception.NotFound;

public class ServerNotFoundException extends RuntimeException {
    public ServerNotFoundException(String message) {
        super(message);
    }
}
