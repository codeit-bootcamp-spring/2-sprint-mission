package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUserNameException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(DuplicateUserNameException.class)
    public String handleDuplicateUserName(DuplicateUserNameException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmail(DuplicateEmailException e) {
        return e.getMessage();
    }
}
