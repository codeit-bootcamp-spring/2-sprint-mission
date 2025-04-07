package com.sprint.mission.discodeit.exceptionHandler;

import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ReadStatusExceptionHandler {

  @ExceptionHandler(ReadStatusAlreadyExistsException.class)
  public ResponseEntity<String> readStatusAlreadyExistsError(ReadStatusAlreadyExistsException e) {
    return ResponseEntity.status(400).body("읽음 상태 오류: " + e.getMessage());
  }

  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ResponseEntity<String> readStatusNotFoundError(ReadStatusNotFoundException e) {
    return ResponseEntity.status(404).body("읽음 상태 오류: " + e.getMessage());
  }
}
