package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.status.user.UserStatusAlreadyExistsError;
import com.sprint.mission.discodeit.exception.status.user.UserStatusNotFoundError;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class UserStatusErrorAdvice {


  @ExceptionHandler(UserStatusNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundError(UserStatusNotFoundError error) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(UserStatusAlreadyExistsError.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsError(
      UserStatusAlreadyExistsError error) {
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

//  @ExceptionHandler(UserStatusError.class)
//  public ResponseEntity<ErrorResponse> handleReadStatusError(UserStatusError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }
}
