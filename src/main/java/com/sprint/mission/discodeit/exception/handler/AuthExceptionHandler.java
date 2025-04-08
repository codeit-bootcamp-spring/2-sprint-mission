package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.exception.custom.auth.InvalidPasswordException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPassword(InvalidPasswordException e) {
        return ResponseEntity.badRequest().body("로그인 오류: " + e.getMessage());
    }
}
