package com.sprint.mission.discodeit.exception;

public enum ResultCode implements Code {
  SUCCESS(200, "SUCCESS"),

  BAD_REQUEST(400, "BAD REQUEST"),
  INVALID_PASSWORD(4000001, "Invalid password"),

  FORBIDDEN(403, "FORBIDDEN"),
  NOT_SUPPORT_API(403, "NOT_SUPPORT_API"),
  NOT_FOUND(404, "NOT FOUND"),

  INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

  SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE"),
  ;

  private int code;
  private String message;

  ResultCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return this.code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
