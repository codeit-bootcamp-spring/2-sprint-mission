package com.sprint.mission.discodeit.Exception.NotFound;

public class ServerNotFoundException extends RuntimeException {
    public ServerNotFoundException(String message) {
        super(message);
    }
}
