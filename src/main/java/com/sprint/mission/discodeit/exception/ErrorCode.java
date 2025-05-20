package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    DUPLICATE_USER(HttpStatus.CONFLICT, "User already exists"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Wrong password"),

    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "Channel not found"),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST, "Private channel cannot be updated"),

    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Message not found"),

    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Binary content not found"),
    BINARY_CONTENT_STORAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error storing binary content"),

    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "Read status not found"),
    READ_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "Read status already exists"),

    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "User status not found"),
    USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "User status already exists"),

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed"),
    FILE_PARSE_ERROR(HttpStatus.BAD_REQUEST, "File parsing failed"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden");

    private final HttpStatus httpStatus;
    private final String message;
}
