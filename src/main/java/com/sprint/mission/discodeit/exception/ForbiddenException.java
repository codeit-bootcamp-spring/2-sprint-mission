package com.sprint.mission.discodeit.exception;

public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String message) {
        super(message);
    }
    
    public ForbiddenException(String resource, String action) {
        super(String.format("You don't have permission to %s this %s", action, resource));
    }
} 