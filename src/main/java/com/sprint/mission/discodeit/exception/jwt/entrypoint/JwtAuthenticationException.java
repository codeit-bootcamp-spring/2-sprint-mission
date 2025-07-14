package com.sprint.mission.discodeit.exception.jwt.entrypoint;

import com.sprint.mission.discodeit.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

  private final int httpStatus;
  private final String errorCode;

  public JwtAuthenticationException(String message, ErrorCode errorCode) {
    super(message);
    this.httpStatus = errorCode.getStatus();
    this.errorCode = errorCode.name();
  }

  public JwtAuthenticationException(String message, Throwable cause, ErrorCode errorCode) {
    super(message, cause);
    this.httpStatus = errorCode.getStatus();
    this.errorCode = errorCode.name();
  }

  public int getHttpStatus() {
    return httpStatus;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
