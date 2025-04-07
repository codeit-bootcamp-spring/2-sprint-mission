package com.sprint.mission.discodeit.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(fieldErrors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("Bad Request", ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleNotSpecificException(Exception ex) {
        log.error("Not SpecificException: {}", ex.getMessage());

        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of("InterServerError", ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        log.error("NullPointerException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력값 중 null이 존재합니다.");
    }
}
