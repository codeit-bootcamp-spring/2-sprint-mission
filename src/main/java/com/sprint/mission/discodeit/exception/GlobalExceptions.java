package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.NotFound.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity globalException(NotFoundException ex) {
        //  TODO.
        HttpStatus message = HttpStatus.valueOf(ex.getMessage());
        return new ResponseEntity<>(message);
    }
}
