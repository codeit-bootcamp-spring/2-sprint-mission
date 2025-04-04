package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundError;
import com.sprint.mission.discodeit.exception.channel.NullPointChannelIdError;
import com.sprint.mission.discodeit.exception.channel.UnmodifiableChannelError;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ChannelErrorAdvice {

  private static final Logger logger = LoggerFactory.getLogger(ChannelErrorAdvice.class);

  @ExceptionHandler(ChannelNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleChannelNotFoundError(ChannelNotFoundError error) {
    logger.error("Channel Not Found handled: ", error);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(UnmodifiableChannelError.class)
  public ResponseEntity<ErrorResponse> handleUnmodifiableChannelError(
      UnmodifiableChannelError error) {
    logger.error("Unmodifiable Channel handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

  @ExceptionHandler(NullPointChannelIdError.class)
  public ResponseEntity<ErrorResponse> handleNullPointChannelIdError(
      NullPointChannelIdError error) {
    logger.error("Null Point Channel ID error handled: ", error);
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

//  @ExceptionHandler(ChannelError.class)
//  public ResponseEntity<ErrorResponse> handleChannelError(ChannelError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }
}
