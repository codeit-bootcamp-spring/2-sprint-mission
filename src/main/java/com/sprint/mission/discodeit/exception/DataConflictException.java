package com.sprint.mission.discodeit.exception;

public class DataConflictException extends RuntimeException {
    

    public DataConflictException(String entityType, String field, Object value) {
        super(String.format("Data conflict for %s with %s: '%s'", entityType, field, value));
    }
} 