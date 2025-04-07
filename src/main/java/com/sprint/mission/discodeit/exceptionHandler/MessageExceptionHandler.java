package com.sprint.mission.discodeit.exceptionHandler;

import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class MessageExceptionHandler {

  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<String> handleMessageNotFoundError(MessageNotFoundException e) {
    return ResponseEntity.status(404).body("메세지 오류: " + e.getMessage());
  }
}
