package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleUserException(DiscodeitException e) {
        log.error("커스텀 예외 발생: code={}, message={}", e.getErrorCode(), e.getMessage(), e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new ErrorResponse(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e, ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("요청 유효성 검사 실패: {}", e.getMessage());
        Map<String, Object> details = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                                (existing, replacement) -> existing
                        )
                );
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        ErrorResponse errorResponse = new ErrorResponse(e, errorCode, details);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        log.error("필수 요청 파라미터 누락: {}", e.getMessage());
        ErrorCode errorCode = ErrorCode.REQUIRED_PARAMETER; // 혹은 적절한 ErrorCode 상수
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(e, errorCode));
    }
}
