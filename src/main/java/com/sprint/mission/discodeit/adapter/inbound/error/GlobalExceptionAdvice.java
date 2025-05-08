package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserLoginFailedException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionAdvice {

  private final MessageSource messageSource;

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(UserNotFoundException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    log.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(UserAlreadyExistsException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    log.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(ChannelUnmodifiableException.class)
  public ResponseEntity<ErrorResponse> handleUnmodifiableException(ChannelUnmodifiableException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    log.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(UserLoginFailedException.class)
  public ResponseEntity<ErrorResponse> handleLoginFailedException(UserLoginFailedException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    log.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleArgumentNotValidException(
      MethodArgumentNotValidException ex, Locale locale) {
    ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;
    String message = messageSource.getMessage(errorCode.getMessage(),
        null, locale);
    ErrorResponse response = ErrorResponse.of(Instant.now(), errorCode, message, ex.toString());
    log.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }
}
