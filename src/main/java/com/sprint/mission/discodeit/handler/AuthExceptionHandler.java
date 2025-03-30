package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.exception.auth.LoginFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(LoginFailedException e) {
        String causeMessage = (e.getCause() != null) ? "\n원인: " + e.getCause() : "";
        return ResponseEntity.status(e.getStatus())
                .body(new ApiResponse<>(
                        false,
                        "예외 발생: " + e.getMessage() + causeMessage,
                        null)
                );
    }
}
