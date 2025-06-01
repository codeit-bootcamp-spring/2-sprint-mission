package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ResponseErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MessageExceptionHandler {

  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ResponseErrorBody> handleMessageNotFoundException(
      MessageNotFoundException e) {
    log.warn("MessageNotFoundException handled: {}, details: {}", e.getMessage(), e.getDetails());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }
}
