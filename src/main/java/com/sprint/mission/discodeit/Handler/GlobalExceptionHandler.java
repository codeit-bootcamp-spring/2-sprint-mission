package com.sprint.mission.discodeit.Handler;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // IllegalArgumentException 처리
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("잘못된 요청: " + ex.getMessage());
  }


  // NoSuchElementException 처리
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<?> handleNoSuchElement(NoSuchElementException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("존재하지 않는 리소스: " + ex.getMessage());
  }

  // 기타 모든 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneralException(Exception ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("서버 오류: " + ex.getMessage());
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<?> handleUnsupportedOperationException(UnsupportedOperationException ex) {
    return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED)  // 405 Method Not Allowed
        .body("지원하지 않는 작업: " + ex.getMessage());
  }

}
