package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.dto.ResponseErrorBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ResponseErrorBody> handleUserNotFound(UserNotFound e) {
        logger.error("{} handled by UserExceptionHandler", e.getMessage());
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseErrorBody(e.getMessage()));
    }
}