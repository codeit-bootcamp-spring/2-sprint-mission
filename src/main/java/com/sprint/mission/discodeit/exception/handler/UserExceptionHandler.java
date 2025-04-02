package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.exception.custom.user.UserEmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.custom.user.UserNameAlreadyExistsException;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(404).body("유저 오류: " + e.getMessage());
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailExists(UserEmailAlreadyExistsException e) {
        return ResponseEntity.badRequest().body("유저 이메일 오류: " + e.getMessage());
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<String> handleNameExists(UserNameAlreadyExistsException e) {
        return ResponseEntity.badRequest().body("유저 이름 오류: " + e.getMessage());
    }
}
