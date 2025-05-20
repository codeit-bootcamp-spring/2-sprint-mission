package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    log.warn("유효성 검증 실패: {}", ex.getMessage());

    Map<String, Object> details = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            fieldError -> fieldError.getField(),
            fieldError -> fieldError.getDefaultMessage(),
            (msg1, msg2) -> msg1 // 중복 필드 발생 시 첫 번째 메시지 유지
        ));

    ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        errorCode.name(),
        "유효성 검증에 실패했습니다.",
        details,
        ex.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );

    return ResponseEntity.badRequest().body(response);
  }

}
