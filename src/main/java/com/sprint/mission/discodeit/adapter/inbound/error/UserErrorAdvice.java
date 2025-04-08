package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.user.NullPointUserIdError;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsError;
import com.sprint.mission.discodeit.exception.user.UserLoginFailedError;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Hidden
@RestControllerAdvice
public class UserErrorAdvice {

  private static final Logger logger = LoggerFactory.getLogger(UserErrorAdvice.class);


  @ExceptionHandler(UserNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundError(UserNotFoundError error) {
    logger.error("UserNotFound handled: ", error);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(UserAlreadyExistsError.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsError(UserAlreadyExistsError error) {
    logger.error("User Already Exists handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

  @ExceptionHandler(UserLoginFailedError.class)
  public ResponseEntity<ErrorResponse> handleUserLoginFailedError(UserLoginFailedError error) {
    logger.error("User Login Failed Error handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

  @ExceptionHandler(NullPointUserIdError.class)
  public ResponseEntity<ErrorResponse> handleNullPointUserIdError(NullPointUserIdError error) {
    logger.error("Null Point User ID error handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

//  @ExceptionHandler(UserNotFoundError.class)
//  public ResponseEntity<ErrorResponse> handleUserError(UserError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }

}
