package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.status.user.NullPointUserStatusIdError;
import com.sprint.mission.discodeit.exception.status.user.UserStatusAlreadyExistsError;
import com.sprint.mission.discodeit.exception.status.user.UserStatusNotFoundError;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class UserStatusErrorAdvice {

  private static final Logger logger = LoggerFactory.getLogger(UserStatusErrorAdvice.class);

  @ExceptionHandler(UserStatusNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleUserStatusNotFoundError(
      UserStatusNotFoundError error) {
    logger.error("User Status Not Found handled: ", error);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(UserStatusAlreadyExistsError.class)
  public ResponseEntity<ErrorResponse> handleUserStatusAlreadyExistsError(
      UserStatusAlreadyExistsError error) {
    logger.error("User Status Already Exists handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

  @ExceptionHandler(NullPointUserStatusIdError.class)
  public ResponseEntity<ErrorResponse> handleNullPointUserStatusIdError(
      NullPointUserStatusIdError error) {
    logger.error("Null Point User Status ID error handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

//  @ExceptionHandler(UserStatusError.class)
//  public ResponseEntity<ErrorResponse> handleReadStatusError(UserStatusError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }
}
