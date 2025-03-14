package com.sprint.mission.discodeit.handler;


import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.DuplicateUserNameException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
