package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // USER
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
  DUPLICATE_USER(HttpStatus.CONFLICT, "Duplicate User"),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email Already Exists"),
  USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username Already Exists"),
  PASSWORD_NOT_CORRECT(HttpStatus.UNAUTHORIZED, "Password Not Correct"),


  // CHANNEL
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "Channel Not Found"),
  PRIVATE_CHANNEL_UPDATE(HttpStatus.FORBIDDEN, "Cannot Update Private Channel"),

  // MESSAGE
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Message Not Found"),


  // BINARY CONTENT
  FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "File Upload Failed"),
  BINARY_CONTENT_NOT_FOUND(HttpStatus.CONFLICT, "Binary Content Not Found"),

  // READ STATUS
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "ReadStatus Not Found"),

  // USER STATUS
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "User Status Not Found"),
  USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "User Status Already Exists");


  private HttpStatus status;
  private String message;

  ErrorCode(HttpStatus httpStatus, String message) {
    this.status = httpStatus;
    this.message = message;
  }
}
