package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.core.channel.exception.ChannelInvalidRequestException;
import com.sprint.mission.discodeit.core.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import com.sprint.mission.discodeit.core.message.exception.MessageInvalidRequestException;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserInvalidRequestException;
import com.sprint.mission.discodeit.core.user.exception.UserLoginFailedException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
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

  private ErrorResponse createResponse(DiscodeitException ex, Object[] args,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();
    String message = messageSource.getMessage(errorCode.getMessage(), args, locale);
    ErrorResponse response = ErrorResponse.of(ex.getTimestamp(), errorCode, message, ex.toString());
    log.error(message);
    return response;
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(UserNotFoundException ex,
      Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(UserAlreadyExistsException ex,
      Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }

  @ExceptionHandler(UserLoginFailedException.class)
  public ResponseEntity<ErrorResponse> handleLoginFailedException(UserLoginFailedException ex,
      Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }

  @ExceptionHandler(UserInvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleUserInvalidRequestException(
      UserInvalidRequestException ex,
      Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }

  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(ChannelNotFoundException ex,
      Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }

  @ExceptionHandler(ChannelUnmodifiableException.class)
  public ResponseEntity<ErrorResponse> handleUnmodifiableException(ChannelUnmodifiableException ex,
      Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }

  @ExceptionHandler(ChannelInvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleMessageInvalidRequestException(
      ChannelInvalidRequestException ex, Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }


  @ExceptionHandler(MessageInvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleMessageInvalidRequestException(
      MessageInvalidRequestException ex, Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
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
