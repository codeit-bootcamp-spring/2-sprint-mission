package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.message.MessageError;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MessageErrorAdvice {

  @ExceptionHandler(MessageError.class)
  public ResponseEntity<ErrorResponse> handleMessageError(MessageError error) {
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

  @ExceptionHandler(MessageNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleMessageNotFoundError(MessageNotFoundError error) {
    return ResponseEntity.notFound().build();
  }
}
