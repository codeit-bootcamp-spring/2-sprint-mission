package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.data.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RestException.class)
  public ResponseEntity<ErrorDto> handlerRestException(RestException e) {
    Code code = e.getCode();
    return ResponseEntity.status(mapToHttpStatus(code))
        .body(new ErrorDto(code.getCode(), code.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleUnexpected(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorDto(500, "예상치 못한 서버 오류가 발생했습니다."));
  }

  private HttpStatus mapToHttpStatus(Code code) {
    return switch (code.getCode()) {
      case 400 -> HttpStatus.BAD_REQUEST;
      case 403 -> HttpStatus.FORBIDDEN;
      case 404, 404001 -> HttpStatus.NOT_FOUND;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }
}
