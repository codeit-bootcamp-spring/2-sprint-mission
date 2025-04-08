package com.sprint.mission.discodeit.exceptionHandler;

import com.sprint.mission.discodeit.exception.auth.PasswordMismatchException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class AuthExceptionHandler {

  @ExceptionHandler(PasswordMismatchException.class)
  public ResponseEntity<String> passwordMismatchError(PasswordMismatchException e) {
    return ResponseEntity.status(400).body("비밀번호 오류: " + e.getMessage());
  }
}
