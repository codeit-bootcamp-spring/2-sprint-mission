package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.exception.custom.binaryContent.BinaryContentNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BinaryContentExceptionHandler {

    @ExceptionHandler(BinaryContentNotFoundException.class)
    public ResponseEntity<String> handleBinaryContentNotFound(BinaryContentNotFoundException e) {
        return ResponseEntity.status(404).body("Binary content 오류: " + e.getMessage());
    }
}
