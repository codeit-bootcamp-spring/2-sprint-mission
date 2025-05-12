package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(
            errorCode.getCode(),
            errorCode.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException e) {
        Map<String, Object> details = Map.of(
            "errors", e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> Map.of(
                    "field", fieldError.getField(),
                    "message", fieldError.getDefaultMessage()
                ))
                .toList()
        );

        ErrorResponse response = ErrorResponse.of(
            "VALIDATION_FAILED",
            "요청값이 유효하지 않습니다.",
            details,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse response = ErrorResponse.of(
            "INTERNAL_SERVER_ERROR",
            "서버 내부 오류가 발생했습니다.",
            Map.of(),
            e.getClass().getSimpleName(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
