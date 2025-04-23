package com.sprint.discodeit.sprint.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("UNEXPECTED", "예상치 못한 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ErrorResponse> requestException(RequestException e) {
        return buildErrorResponse(e);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> serverException(ServerException e) {
        return buildErrorResponse(e);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> authException(AuthException e) {
        return buildErrorResponse(e);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(BaseException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }
}
