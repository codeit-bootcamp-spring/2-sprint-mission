package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.dto.BaseResponseDto;
import com.sprint.mission.discodeit.exception.handler.custom.user.UserEmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.handler.custom.user.UserNameAlreadyExistsException;
import com.sprint.mission.discodeit.exception.handler.custom.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponseDto> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(404).body(BaseResponseDto.failure("유저 오류: " + e.getMessage()));
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<BaseResponseDto> handleEmailExists(UserEmailAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(BaseResponseDto.failure("이메일 오류: " + e.getMessage()));
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<BaseResponseDto> handleNameExists(UserNameAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(BaseResponseDto.failure("이름 오류: " + e.getMessage()));
    }
}
