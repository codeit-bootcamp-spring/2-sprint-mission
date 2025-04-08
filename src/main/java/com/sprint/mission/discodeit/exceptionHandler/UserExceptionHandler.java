package com.sprint.mission.discodeit.exceptionHandler;

import com.sprint.mission.discodeit.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.NameAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class UserExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundError(UserNotFoundException e) {
    return ResponseEntity.status(404).body("유저 오류: " + e.getMessage());
  }

  @ExceptionHandler(NameAlreadyExistsException.class)
  public ResponseEntity<String> handleUserNameAlreadyExistsError(NameAlreadyExistsException e) {
    return ResponseEntity.status(400).body("유저 오류: " + e.getMessage());
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<String> handleUserEmailAlreadyExistsError(EmailAlreadyExistsException e) {
    return ResponseEntity.status(400).body("유저 오류: " + e.getMessage());
  }

}
