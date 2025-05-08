package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  INTERNAL_SERVER_ERROR(500, "S001", "Internal Server Error"),
  METHOD_ARGUMENT_NOT_VALID(400, "V000", "error.Argument"),

  USER_ERROR(400, "U000", "error.user.error"),
  USER_NOT_FOUND(404, "U001", "error.user.not-found"),
  LOGIN_FAILED(400, "U002", "error.user.login-failed"),
  USER_NAME_ALREADY_EXISTS(400, "U003", "error.user.name-already-exists"),
  USER_EMAIL_ALREADY_EXISTS(400, "U004", "error.user.email-already-exists"),

  CHANNEL_NOT_FOUND(404, "C001", "error.channel.not-found"),
  UNMODIFIABLE_ERROR(400, "C002", "error.channel.unmodifiable"),

  USER_STATUS_NOT_FOUND(404, "US001", "error.user-status.not-found"),
  USER_STATUS_ALREADY_EXISTS(400, "US002", "error.user-status.already-exists"),

  READ_STATUS_NOT_FOUND(404, "RS001", "error.read-status.not-found"),
  READ_STATUS_ALREADY_EXISTS(400, "RS002", "error.read-status.already-exists"),


  MESSAGE_NOT_FOUND(404, "M001", "error.message.not-found"),

  FILE_NOT_FOUND(404, "F001", "error.channel.not-found");


  private final int httpStatus;
  private final String code;
  private final String message;

}
