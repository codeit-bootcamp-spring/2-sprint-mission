package com.sprint.mission.discodeit.exception.userstatus;

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
public class UserStatusHandler {

  @ExceptionHandler(UserStatusNotFoundException.class)
  public ResponseEntity<ResponseErrorBody> handleUserStatusNotFoundException(
      UserStatusNotFoundException e) {
    log.warn("UserStatusNotFoundException handled: {}, details: {}", e.getMessage(),
        e.getDetails());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }


  @ExceptionHandler(DuplicateUserStatusException.class)
  public ResponseEntity<ResponseErrorBody> handleDuplicateUserStatusException(
      DuplicateUserStatusException e) {
    log.warn("DuplicateUserStatusException handled: {}, details: {}", e.getMessage(),
        e.getDetails());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }

}
