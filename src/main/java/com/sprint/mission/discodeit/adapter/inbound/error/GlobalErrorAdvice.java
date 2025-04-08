package com.sprint.mission.discodeit.adapter.inbound.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorAdvice {

  private static final Logger logger = LoggerFactory.getLogger(GlobalErrorAdvice.class);

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ErrorResponse> handleMessageNotFoundError(NullPointerException error) {
    logger.error("NullPoint handled: ", error);
    return ResponseEntity.internalServerError().build();
  }
}
