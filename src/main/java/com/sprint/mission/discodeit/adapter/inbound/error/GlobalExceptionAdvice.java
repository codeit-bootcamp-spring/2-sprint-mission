package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.core.user.exception.UserLoginFailedException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {

  private final MessageSource messageSource;

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(UserNotFoundException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    logger.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(UserAlreadyExistsException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    logger.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(ChannelUnmodifiableException.class)
  public ResponseEntity<ErrorResponse> handleUnmodifiableException(ChannelUnmodifiableException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    logger.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(UserLoginFailedException.class)
  public ResponseEntity<ErrorResponse> handleLoginFailedException(UserLoginFailedException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    logger.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

}
