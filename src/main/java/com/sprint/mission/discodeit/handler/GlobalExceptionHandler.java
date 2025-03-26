package com.sprint.mission.discodeit.handler;


import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.DuplicateUserNameException;
import com.sprint.mission.discodeit.exception.InvalidCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 컨트롤러에 계층에서 발생한 예외를 처리
// 현재 컨트롤러가 없어서 의미가 없을 듯
// @Aspect, JoinPoint를 통해 처리를 할까 했지만 별 의미는 없어보임.
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateUserNameException.class)
    public String handleDuplicateUserName(DuplicateUserNameException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmail(DuplicateEmailException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException e) {
        return e.getMessage();
    }
}
