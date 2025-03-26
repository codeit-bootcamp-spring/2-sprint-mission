package com.sprint.mission.discodeit.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public enum ResultCode implements Code{
    SUCCESS(200, "SUCCESS"),

    BAD_REQUEST (400, "BAD REQUEST"),

    UNAUTHORIZED_PRIVATE_CHANNEL(401, "Private channel is unauthorized"),
    INVALID_PASSWORD(401, "Invalid password"),

    USER_NOT_FOUND (404, "User not found"),
    CHANNEL_NOT_FOUND (404, "Channel not found"),
    MESSAGE_NOT_FOUND (404, "Message not found"),
    BINARY_CONTENT_NOT_FOUND (404, "BinaryContent not found"),
    READ_STATUS_NOT_FOUND(404, "ReadStatus not found"),
    USER_STATUS_NOT_FOUND(404, "UserStatus not found"),

    DUPLICATE_USERNAME(409, "Username exists already"),
    DUPLICATE_EMAIL(409, "Email exists already"),
    DUPLICATE_READ_STATUS(409, "ReadStatus exists already"),
    DUPLICATE_USER_STATUS(409, "UserStatus exists already");

    private final int status;
    private final String message;

    ResultCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
