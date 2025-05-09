package com.sprint.mission.discodeit.exception.user;

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
public class UserExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ResponseErrorBody> handleUserNotFound(UserNotFoundException e) {
    log.warn("UserNotFoundException handled: {}, details: {}", e.getMessage(), e.getDetails());
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ResponseErrorBody> handleInvalidPassword(InvalidPasswordException e) {
    log.warn("InvalidPasswordException handled: {}, details: {}", e.getMessage(), e.getDetails());
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ResponseErrorBody> handleDuplicateEmail(DuplicateEmailException e) {
    log.warn("DuplicateEmailException handled: {}, details: {}", e.getMessage(), e.getDetails());
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(DuplicateUsernameException.class)
  public ResponseEntity<ResponseErrorBody> handleDuplicateUsername(DuplicateUsernameException e) {
    log.warn("DuplicateUsernameException handled: {}, details: {}", e.getMessage(), e.getDetails());
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }
}
