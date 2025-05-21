package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleOther(MethodArgumentNotValidException ex) {
    int statusCode = ErrorCode.BAD_REQUEST.getHttpStatus().value();

    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getDefaultMessage())
            .orElse("잘못된 요청입니다.");

    ErrorResponse body = ErrorResponse.builder()
            .timestamp(Instant.now())
            .code(ErrorCode.BAD_REQUEST.name())
            .message(errorMessage)
            .details(Map.of("exception", ex.getMessage()))
            .exceptionType(ex.getClass().getSimpleName())
            .status(statusCode)
            .build();

    return ResponseEntity.status(statusCode).body(body);

  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleOther(DiscodeitException ex) {
    int statusCode = ex.getErrorCode().getHttpStatus().value();

    ErrorResponse body = ErrorResponse.builder()
            .timestamp(ex.getTimestamp())
            .code(ex.getErrorCode().getHttpStatus().name())
            .message(ex.getErrorCode().getMessage())
            .details(ex.getDetails())
            .exceptionType(ex.getClass().getSimpleName())
            .status(statusCode)
            .build();


    return ResponseEntity.status(statusCode).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
    int statusCode = ErrorCode.INTERNAL_ERROR.getHttpStatus().value();

    ErrorResponse body = ErrorResponse.builder()
            .timestamp(Instant.now())
            .code(ErrorCode.INTERNAL_ERROR.name())
            .message(ErrorCode.INTERNAL_ERROR.getMessage())
            .details(Map.of("exception", ex.getMessage()))
            .exceptionType(ex.getClass().getSimpleName())
            .status(statusCode)
            .build();


    return ResponseEntity.status(statusCode).body(body);
  }
}
