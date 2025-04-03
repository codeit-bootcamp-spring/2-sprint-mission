package com.sprint.mission.discodeit.controller.advice;

import com.sprint.mission.discodeit.dto.ApiDataResponse;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiDataResponse<?>> handleGeneralException(Exception e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiDataResponse.error("서버 오류: " + e.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiDataResponse<?>> handleIllegalArgumentException(
      IllegalArgumentException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiDataResponse.error("잘못된 요청: " + e.getMessage()));
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ApiDataResponse<?>> handleNullPointerException(NullPointerException e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiDataResponse.error("NULL 값 오류: " + e.getMessage()));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiDataResponse<?>> handleNoSuchElementException(NoSuchElementException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ApiDataResponse.error("리소스를 찾을 수 없음: " + e.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiDataResponse<?>> handleRuntimeException(RuntimeException e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiDataResponse.error("예상치 못한 오류 발생: " + e.getMessage()));
  }
}