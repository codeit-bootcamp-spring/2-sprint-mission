package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // User
    USER_NOT_FOUND(404, "U001", "User not found."),
    USER_EMAIL_EXISTS(400, "U002", "Email already exists."),
    USER_NAME_EXISTS(400, "U003", "Username already exists."),

    // Channel
    CHANNEL_NOT_FOUND(404, "C001", "Channel not found."),
    CHANNEL_ALREADY_EXISTS(400, "C002", "Channel already exists."),
    PRIVATE_CHANNEL_UPDATE_NOT_SUPPORTED(400, "C003", "Private channel update not supported."),

    // Message
    MESSAGE_NOT_FOUND(404, "M001", "Message not found."),

    // ReadStatus
    READ_STATUS_NOT_FOUND(404, "R001", "Read status not found."),
    READ_STATUS_ALREADY_EXISTS(400, "R002", "Read status already exists."),

    // UserStatus
    USER_STATUS_NOT_FOUND(404, "US001", "UserStatus not found."),
    USER_STATUS_ALREADY_EXISTS(400, "US002", "UserStatus already exists."),

    // Auth
    INVALID_PASSWORD(400, "A001", "Password is invalid."),

    // BinaryContent
    BINARY_CONTENT_NOT_FOUND(404, "B001", "BinaryContent not found."),
    BINARY_CONTENT_SAVE_FAILED(500, "B002", "BinaryContent save failed."),
    BINARY_CONTENT_LOAD_FAILED(500, "B003", "BinaryContent load failed."),
    BINARY_CONTENT_DOWNLOAD_FAILED(500, "B004", "BinaryContent download failed."),
    BINARY_CONTENT_DELETE_FAILED(500, "B005", "BinaryContent delete failed."),

    // Server
    INTERNAL_SERVER_ERROR(500, "S001", "Internal server error."),

    // Validation
    INVALID_INPUT_VALUE(400, "S002", "Invalid input value."),
    REQUIRED_PARAMETER(400, "S003", "Required parameter is missing.");


    private final int httpStatus;
    private final String code;
    private final String message;
}