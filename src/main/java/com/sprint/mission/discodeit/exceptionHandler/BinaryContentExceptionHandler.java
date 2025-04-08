package com.sprint.mission.discodeit.exceptionHandler;

import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class BinaryContentExceptionHandler {

  public ResponseEntity<String> binaryContentNotFoundError(BinaryContentNotFoundException e) {
    return ResponseEntity.status(404).body("첨부파일 오류: " + e.getMessage());
  }
}
