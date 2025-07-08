package com.sprint.mission.discodeit.exception;

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
    String message = messageSource.getMessage(ex.getErrorCode().getMessage(), args, locale);
    ErrorResponse response = ErrorResponse.from(ex, message);
    log.error(message);
    return response;
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(DiscodeitException ex,
      Locale locale) {
    ErrorResponse response = createResponse(ex, ex.getArgs(), locale);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleArgumentNotValidException(
      MethodArgumentNotValidException ex, Locale locale) {
    ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;

    String message = messageSource.getMessage(errorCode.getMessage(),
        null, locale);

    ErrorResponse response = ErrorResponse.from(ex, errorCode, message);

    log.error(message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }
}
