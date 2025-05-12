package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.auth.AuthException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentException;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusException;
import com.sprint.mission.discodeit.exception.storage.StorageException;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final View error;

  public GlobalExceptionHandler(View error) {
    this.error = error;
  }

  @ExceptionHandler({
      AuthException.class,
      BinaryContentException.class,
      ChannelException.class,
      MessageException.class,
      ReadStatusException.class,
      StorageException.class,
      UserException.class,
      UserStatusException.class
  })
  public ResponseEntity<ErrorResponse> handleException(DiscodeitException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getTimestamp(),
        e.getErrorCode().name(),
        e.getErrorCode().getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getErrorCode().getHttpStatus().value()
    );
    return ResponseEntity
        .status(e.getErrorCode().getHttpStatus())
        .body(errorResponse);
  }
}
