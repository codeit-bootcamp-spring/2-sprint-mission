package com.sprint.mission.discodeit.controller.advice;

import com.sprint.mission.discodeit.exceptions.readstatus.DuplicateReadStatusException;
import com.sprint.mission.discodeit.exceptions.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.service.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReadStatusExceptionControllerAdvice {

    @ExceptionHandler(ReadStatusNotFoundException.class)
    public ResponseEntity<Object> handleReadStatusNotFoundException(ReadStatusNotFoundException ex) {
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

    @ExceptionHandler(DuplicateReadStatusException.class)
    public ResponseEntity<Object> handleDuplicateReadStatusException(DuplicateReadStatusException ex) {
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
