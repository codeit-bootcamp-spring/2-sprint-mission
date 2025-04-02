package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.status.read.ReadStatusAlreadyExistsError;
import com.sprint.mission.discodeit.exception.status.read.ReadStatusNotFoundError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReadStatusErrorAdvice {

  @ExceptionHandler(ReadStatusNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundError(ReadStatusNotFoundError error) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(ReadStatusAlreadyExistsError.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsError(
      ReadStatusAlreadyExistsError error) {
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

//  @ExceptionHandler(ReadStatusError.class)
//  public ResponseEntity<ErrorResponse> handleReadStatusError(ReadStatusError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }


}
