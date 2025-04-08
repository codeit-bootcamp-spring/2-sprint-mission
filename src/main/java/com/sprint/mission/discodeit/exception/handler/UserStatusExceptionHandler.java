package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.exception.custom.user.UserEmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.custom.user.UserNameAlreadyExistsException;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.custom.userStatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.custom.userStatus.UserStatusNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserStatusExceptionHandler {

    @ExceptionHandler(UserStatusNotFoundException.class)
    public ResponseEntity<String> handleUserStatusNotFound(UserStatusNotFoundException e) {
        return ResponseEntity.status(404).body("유저 상태 오류: " + e.getMessage());
    }

    @ExceptionHandler(UserStatusAlreadyExistsException.class)
    public ResponseEntity<String> handleUserStatusAlreadyExists(UserStatusAlreadyExistsException e) {
        return ResponseEntity.badRequest().body("유저 상태 오류: " + e.getMessage());
    }
}
