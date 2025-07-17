package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ResponseErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ResponseErrorBody> handleAccessDeniedException(AccessDeniedException e) {
    log.warn("AccessDeniedException handled: {}, details: {}", e.getMessage(),
        e.getDetails());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }
}
