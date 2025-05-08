package com.sprint.mission.discodeit.controller.advice;

import com.sprint.mission.discodeit.exceptions.Auth.AuthFailException;
import com.sprint.mission.discodeit.service.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionControllerAdvice {

    @ExceptionHandler(AuthFailException.class)
    public ResponseEntity<ErrorResponse> handleAuthFailException(AuthFailException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(
                        ex.getTimestamp(),
                        ex.getErrorCode().toString(),
                        ex.getErrorCode().getMessage(),
                        ex.getDetails(),
                        ex.getClass().getSimpleName(),
                        HttpStatus.UNAUTHORIZED.value()));
    }
}
