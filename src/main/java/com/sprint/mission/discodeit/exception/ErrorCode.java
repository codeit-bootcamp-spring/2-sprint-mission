package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  // USER ERROR CODE
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "User not found"),
  DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT, "U002", "Duplicate user email"),
  DUPLICATE_USER_USERNAME(HttpStatus.CONFLICT, "U003", "Duplicate user username"),
  PROFILE_UPLOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "U004", "Profile upload failure"),
  INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U005", "Invalid password"),

  // CHANNEL ERROR CODE
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "Channel not found"),
  CHANNEL_UPDATE_ALLOWED(HttpStatus.FORBIDDEN, "C002", "Channel update is not allowed"),

  // MESSAGE ERROR CODE
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M003", "Message not found"),

  // USERSTATUS ERROR CODE
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "US001", "User status not found"),
  USER_STATUS_ALREADY_EXIST(HttpStatus.CONFLICT, "US002", "User status already exist"),

  // READSTATUS ERROR CODE
  READ_STATUS_ALREADY_EXIST(HttpStatus.CONFLICT, "R001", "Read status already exist"),
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "R002", "Read status not found"),

  // BINARYCONTENT ERROR CODE
  BINARY_CONTENT_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "B001",
      "Binary content processing error"),
  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "B002", "Binary content not found");


  private final HttpStatus status;
  private final String code;
  private final String message;
}
