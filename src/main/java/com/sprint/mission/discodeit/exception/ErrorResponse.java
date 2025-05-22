package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public class ErrorResponse {
  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;
  private final int status;

  public ErrorResponse(DiscodeitException exception, int status) {
    this.timestamp = Instant.now();
    this.code = exception.getErrorCode().name();
    this.message = exception.getMessage();
    this.details = exception.getDetails();
    this.exceptionType = exception.getClass().getSimpleName();
    this.status = status;
  }

  public ErrorResponse(Exception exception, int status) {
    this.timestamp = Instant.now();
    this.code = exception.getClass().getSimpleName();
    this.message = exception.getMessage();
    this.details = new HashMap<>();
    this.exceptionType = exception.getClass().getSimpleName();
    this.status = status;
  }

  public ErrorResponse(MethodArgumentNotValidException exception,
      Map<String,Object> details, int status) {
    this.timestamp     = Instant.now();
    this.code          = exception.getClass().getSimpleName();
    this.message       = exception.getMessage();
    this.details       = details;
    this.exceptionType = exception.getClass().getSimpleName();
    this.status        = status;
  }
}
