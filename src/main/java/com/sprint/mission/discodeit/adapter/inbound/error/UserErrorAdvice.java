package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsError;
import com.sprint.mission.discodeit.exception.user.UserLoginFailedError;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Hidden
@RestControllerAdvice
public class UserErrorAdvice {

//  private static final Logger logger = LoggerFactory.getLogger(UserErrorAdvice.class);


  @ExceptionHandler(UserNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundError(UserNotFoundError error) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(UserAlreadyExistsError.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsError(UserAlreadyExistsError error) {
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

  @ExceptionHandler(UserLoginFailedError.class)
  public ResponseEntity<ErrorResponse> handleUserLoginFailedError(UserLoginFailedError error) {
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

//  @ExceptionHandler(UserNotFoundError.class)
//  public ResponseEntity<ErrorResponse> handleUserError(UserError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }

}
