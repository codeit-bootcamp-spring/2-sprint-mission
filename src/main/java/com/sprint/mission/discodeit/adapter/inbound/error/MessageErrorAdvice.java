package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.message.MessageNotFoundError;
import com.sprint.mission.discodeit.exception.message.NullPointMessageIdError;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class MessageErrorAdvice {

  private static final Logger logger = LoggerFactory.getLogger(MessageErrorAdvice.class);

  @ExceptionHandler(MessageNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleMessageNotFoundError(MessageNotFoundError error) {
    logger.error("Message Not Found handled: ", error);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(NullPointMessageIdError.class)
  public ResponseEntity<ErrorResponse> handleNullPointMessageIdError(
      NullPointMessageIdError error) {
    logger.error("Null Point Message ID error handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }
//  @ExceptionHandler(MessageError.class)
//  public ResponseEntity<ErrorResponse> handleMessageError(MessageError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }
}
