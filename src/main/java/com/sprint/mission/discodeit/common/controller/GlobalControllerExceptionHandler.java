package com.sprint.mission.discodeit.common.controller;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        log.error("Wrong Method Argument: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(ErrorResponse.ofCustomException(fieldErrors));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingPart(MissingServletRequestPartException ex) {
        log.error("Missing multipart part: {}", ex.getRequestPartName());

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), ex, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFound(NoResourceFoundException ex) {
        log.error("No static Resource: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), ex, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
        log.error("Illegal argument: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(ErrorResponse.ofCustomException(HttpStatus.BAD_REQUEST.toString(), ex, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception unexpectedException) {
        log.error("Not SpecificException: {}", unexpectedException.getMessage());

        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        unexpectedException,
                        HttpStatus.INTERNAL_SERVER_ERROR.value())
                );
    }

}
