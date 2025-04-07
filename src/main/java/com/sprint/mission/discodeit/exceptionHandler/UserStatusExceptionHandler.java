package com.sprint.mission.discodeit.exceptionHandler;

import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class UserStatusExceptionHandler {

  @ExceptionHandler(UserStatusNotFoundException.class)
  public ResponseEntity<String> userStatusNotFoundError(UserStatusNotFoundException e) {
    return ResponseEntity.status(404).body("유저 상태 오류: " + e.getMessage());
  }
}
