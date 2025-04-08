package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.exception.custom.message.MessageNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MessageExceptionHandler {

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<String> handleMessageNotFound(MessageNotFoundException e) {
        return ResponseEntity.status(404).body("메시지 오류: " + e.getMessage());
    }
}
