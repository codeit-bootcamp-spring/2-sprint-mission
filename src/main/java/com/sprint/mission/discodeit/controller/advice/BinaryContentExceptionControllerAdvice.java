package com.sprint.mission.discodeit.controller.advice;

import com.sprint.mission.discodeit.exceptions.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.service.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BinaryContentExceptionControllerAdvice {

    @ExceptionHandler(BinaryContentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBinaryContentNotFoundException(BinaryContentNotFoundException ex) {
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
}
