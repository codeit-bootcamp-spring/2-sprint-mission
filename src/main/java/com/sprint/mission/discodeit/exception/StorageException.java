package com.sprint.mission.discodeit.exception;

public class StorageException extends RuntimeException {
    private final ErrorCode errorCode;

    public StorageException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
