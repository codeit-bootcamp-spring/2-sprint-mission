package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.AlreadyExistsException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.LoginFailedException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.exception.UnmodifiableException;
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

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode, message);
    logger.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode, message);
    logger.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(UnmodifiableException.class)
  public ResponseEntity<ErrorResponse> handleUnmodifiableException(UnmodifiableException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode, message);
    logger.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  @ExceptionHandler(LoginFailedException.class)
  public ResponseEntity<ErrorResponse> handleLoginFailedException(LoginFailedException ex,
      Locale locale) {
    ErrorCode errorCode = ex.getErrorCode();

    String message = messageSource.getMessage(errorCode.getMessage(), ex.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode, message);
    logger.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }


}
