package com.sprint.mission.discodeit.controller.advice;

import com.sprint.mission.discodeit.exceptions.user.DuplicateUserOrEmailException;
import com.sprint.mission.discodeit.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        ex.getTimestamp(),
                        ex.getErrorCode().toString(),
                        ex.getErrorCode().getMessage(),
                        ex.getDetails(),
                        ex.getClass().getSimpleName(),
                        HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(DuplicateUserOrEmailException.class)
    public ResponseEntity<ErrorResponse> handleUserOrEmailNotFoundException(DuplicateUserOrEmailException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(
                        ex.getTimestamp(),
                        ex.getErrorCode().toString(),
                        ex.getErrorCode().getMessage(),
                        ex.getDetails(),
                        ex.getClass().getSimpleName(),
                        HttpStatus.CONFLICT.value()));
    }
}
