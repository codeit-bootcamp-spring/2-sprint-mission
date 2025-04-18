package com.sprint.mission.discodeit.exceptionHandler;

import com.sprint.mission.discodeit.exception.channel.ChannelNameAlreadyExistsException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.MessageNotExistsInChannelException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ChannelExceptionHandler {

  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<String> handleChannelNotFoundError(ChannelNotFoundException e) {
    return ResponseEntity.status(404).body("채널 오류: " + e.getMessage());
  }

  @ExceptionHandler(PrivateChannelUpdateException.class)
  public ResponseEntity<String> privateChannelUpdateError(PrivateChannelUpdateException e) {
    return ResponseEntity.status(400).body("채널 오류: " + e.getMessage());
  }

  @ExceptionHandler(ChannelNameAlreadyExistsException.class)
  public ResponseEntity<String> channelNameAlreadyExistsError(ChannelNameAlreadyExistsException e) {
    return ResponseEntity.status(400).body("채널 오류: " + e.getMessage());
  }

  @ExceptionHandler(MessageNotExistsInChannelException.class)
  public ResponseEntity<String> messageNotExistsInChannelError(
      MessageNotExistsInChannelException e) {
    return ResponseEntity.status(400).body("채널 오류: " + e.getMessage());
  }
}
