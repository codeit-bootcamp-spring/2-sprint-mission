package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.content.BinaryContentNotFoundError;
import com.sprint.mission.discodeit.exception.content.NullPointBinaryContentIdError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BinaryContentErrorAdvice {

  private static final Logger logger = LoggerFactory.getLogger(BinaryContentErrorAdvice.class);

  @ExceptionHandler(BinaryContentNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleBinaryContentNotFoundError(
      BinaryContentNotFoundError error) {
    logger.error("Binary Content not found handled: ", error);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(NullPointBinaryContentIdError.class)
  public ResponseEntity<ErrorResponse> handleNullPointBinaryContentIdError(
      NullPointBinaryContentIdError error) {
    logger.error("Null Point Binary Content ID error handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

}
