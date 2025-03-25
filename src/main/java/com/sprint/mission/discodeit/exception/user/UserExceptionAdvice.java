package com.sprint.mission.discodeit.exception.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ResponseErrorBody> handleUserNotFound(UserNotFound e) {
        logger.error("{} handled by UserExceptionAdvice", e.getMessage());
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseErrorBody(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorBody> handleAllExceptions(Exception e) {
        logger.error("{} handled by UserExceptionAdvice", e.getMessage());
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseErrorBody(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorBody> handleValidationException(MethodArgumentNotValidException e) {
        logger.error("{} handled by UserExceptionAdvice", e.getMessage());
        e.printStackTrace();

        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessage.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append(", ");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseErrorBody(errorMessage.toString()));
    }
}
