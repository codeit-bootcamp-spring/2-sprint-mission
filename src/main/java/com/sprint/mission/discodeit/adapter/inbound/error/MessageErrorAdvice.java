package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.message.MessageNotFoundError;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class MessageErrorAdvice {

  @ExceptionHandler(MessageNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleMessageNotFoundError(MessageNotFoundError error) {
    return ResponseEntity.notFound().build();
  }

//  @ExceptionHandler(MessageError.class)
//  public ResponseEntity<ErrorResponse> handleMessageError(MessageError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }
}
