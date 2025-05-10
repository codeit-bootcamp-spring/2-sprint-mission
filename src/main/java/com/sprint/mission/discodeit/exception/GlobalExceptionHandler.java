package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final UserStatusService userStatusService;

    public GlobalExceptionHandler(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleException(DiscodeitException e) {
        HttpStatus status = mapToHttpStatus(e.getErrorCode());

        ErrorResponse response = new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().name(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            status.value()
        );
        return ResponseEntity.status(status).body(response);
    }

    // errorCode에 따라 HTTPStatus를 정한다.
    private HttpStatus mapToHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case USER_NOT_FOUND, CHANNEL_NOT_FOUND, INFO_NOT_FOUND ->
                HttpStatus.NOT_FOUND; // 404 NOT FOUND
            case INFO_DUPLICATE, PRIVATE_CHANNEL_UPDATE, INVALID_REQUEST ->
                HttpStatus.BAD_REQUEST; // 400 BAD REQUEST
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();

        // 검증 실패한 필드의 리스트 추출 진행 후
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            // 어떤 필드가 실패했는지, 어노테이션에 작성된 오류 메시지
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            "VALIDATION_FAILED",
            "요청 값이 유효하지 않습니다.",
            errors,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /*
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        e.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleException(NoSuchElementException e) {
        e.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
    }
    */
}
