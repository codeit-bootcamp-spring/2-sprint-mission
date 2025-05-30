package com.sprint.mission.discodeit.exception.channel;

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
public class ChannelExceptionHandler {

  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ResponseErrorBody> handleChannelNotFoundException(
      ChannelNotFoundException e) {
    log.warn("ChannelNotFoundException handled: {}, details: {}", e.getMessage(), e.getDetails());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(PrivateChannelUpdateException.class)
  public ResponseEntity<ResponseErrorBody> handlePrivateChannelUpdateException(
      PrivateChannelUpdateException e) {
    log.warn("PrivateChannelUpdateException handled: {}, details: {}", e.getMessage(),
        e.getDetails());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }
}
