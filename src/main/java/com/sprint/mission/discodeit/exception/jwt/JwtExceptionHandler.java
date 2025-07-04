package com.sprint.mission.discodeit.exception.jwt;

import com.sprint.mission.discodeit.exception.ResponseErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtExceptionHandler {

  @ExceptionHandler(InvalidRefreshTokenException.class)
  public ResponseEntity<ResponseErrorBody> handleInvalidRefreshTokenException(
      InvalidRefreshTokenException e) {
    log.warn("InvalidRefreshTokenException handled: {}, details: {}", e.getMessage(),
        e.getDetails());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }
}
