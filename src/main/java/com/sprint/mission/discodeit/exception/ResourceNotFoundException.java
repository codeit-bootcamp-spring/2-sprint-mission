package com.sprint.mission.discodeit.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceType, String fieldName, Object fieldValue) {
        super(String.format("%s with %s : '%s' not found", resourceType, fieldName, fieldValue));
    }
} 