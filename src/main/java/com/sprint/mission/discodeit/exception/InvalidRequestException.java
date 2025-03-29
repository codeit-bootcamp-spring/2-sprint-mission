package com.sprint.mission.discodeit.exception;

public class InvalidRequestException extends RuntimeException {
    
    public InvalidRequestException(String message) {
        super(message);
    }
    
    public InvalidRequestException(String field, String error) {
        super(String.format("Invalid request: %s %s", field, error));
    }
}
