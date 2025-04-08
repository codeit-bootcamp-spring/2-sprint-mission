package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.status.read.NullPointReadStatusIdError;
import com.sprint.mission.discodeit.exception.status.read.ReadStatusAlreadyExistsError;
import com.sprint.mission.discodeit.exception.status.read.ReadStatusNotFoundError;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ReadStatusErrorAdvice {

  private final static Logger logger = LoggerFactory.getLogger(ReadStatusErrorAdvice.class);

  @ExceptionHandler(ReadStatusNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundError(ReadStatusNotFoundError error) {
    logger.error("ReadStatus Not Found handled: ", error);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(ReadStatusAlreadyExistsError.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsError(
      ReadStatusAlreadyExistsError error) {
    logger.error("Read Status Already Exists handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

  @ExceptionHandler(NullPointReadStatusIdError.class)
  public ResponseEntity<ErrorResponse> handleNullReadStatusIdError(
      NullPointReadStatusIdError error) {
    logger.error("Null Point Read Status ID error handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

//  @ExceptionHandler(ReadStatusError.class)
//  public ResponseEntity<ErrorResponse> handleReadStatusError(ReadStatusError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }


}
