package com.sprint.mission.discodeit.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST; // 기본 상태 코드
    // 필요시 도메인별로 다른 상태코드 설정 가능 (예: NOT_FOUND 등)
    return ResponseEntity.status(status)
        .body(new ErrorResponse(ex, status.value()));
  }

  // 그 외 알 수 없는 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex) {
    DiscodeitException unknown = new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR, Map.of()) {
    };
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(unknown, HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }
}
