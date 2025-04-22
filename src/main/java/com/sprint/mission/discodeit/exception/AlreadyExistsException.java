package com.sprint.mission.discodeit.exception;

public class AlreadyExistsException extends NormalException {

  private final ErrorCode errorCode;
  private final Object[] args;

  public AlreadyExistsException(ErrorCode errorCode, Object... args) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.args = args;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public Object[] getArgs() {
    return args;
  }
}
