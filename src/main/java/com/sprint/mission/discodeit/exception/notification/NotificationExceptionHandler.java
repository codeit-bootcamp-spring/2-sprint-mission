package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ResponseErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class NotificationExceptionHandler {

  @ExceptionHandler(NotificationNotFoundException.class)
  public ResponseEntity<ResponseErrorBody> handleNotificationNotFoundException(
      NotificationNotFoundException e) {
    log.warn("NotificationNotFoundException handled: {}, details: {}", e.getMessage(),
        e.getDetails());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }

}
