package com.sprint.mission.discodeit.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_FOUND("User does not exist."),
    DUPLICATE_USER_OR_EMAIL("Username or email already exists."),
    DUPLICATE_CHANNEL("Channel already exists."),
    CHANNEL_NOT_FOUND("Channel does not exist."),
    PRIVATE_CHANNEL_UPDATE("Private channels cannot be changed."),
    MESSAGE_NOT_FOUND("Message does not exist."),
    PROFILE_NOT_FOUND("Profile does not exist."),
    USER_STATUS_NOT_FOUND("UserStatus does not exist."),
    DUPLICATE_READ_STATUS("ReadStatus already exists."),
    READ_STATUS_NOT_FOUND("ReadStatus does not exist."),
    AUTHENTICATION_FAILED("Password does not match.");

    private final String message;

}
