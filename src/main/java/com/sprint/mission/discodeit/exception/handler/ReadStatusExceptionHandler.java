package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.exception.custom.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.custom.readStatus.ReadStatusNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReadStatusExceptionHandler {

    @ExceptionHandler(ReadStatusNotFoundException.class)
    public ResponseEntity<String> handleReadStatusNotFound(ReadStatusNotFoundException e) {
        return ResponseEntity.status(404).body("읽음 상태 오류: " + e.getMessage());
    }

    @ExceptionHandler(ReadStatusAlreadyExistsException.class)
    public ResponseEntity<String> handleReadStatusAlreadyExists(ReadStatusAlreadyExistsException e) {
        return ResponseEntity.badRequest().body("읽음 상태 오류: " + e.getMessage());
    }
}
