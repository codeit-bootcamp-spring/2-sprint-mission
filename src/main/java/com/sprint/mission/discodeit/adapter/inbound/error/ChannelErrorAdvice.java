package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundError;
import com.sprint.mission.discodeit.exception.channel.UnmodifiableChannelError;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ChannelErrorAdvice {


  @ExceptionHandler(ChannelNotFoundError.class)
  public ResponseEntity<ErrorResponse> handleChannelNotFoundError(ChannelNotFoundError error) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(UnmodifiableChannelError.class)
  public ResponseEntity<ErrorResponse> handleUnmodifiableChannelError(
      UnmodifiableChannelError error) {
    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
  }

//  @ExceptionHandler(ChannelError.class)
//  public ResponseEntity<ErrorResponse> handleChannelError(ChannelError error) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
//  }
}
