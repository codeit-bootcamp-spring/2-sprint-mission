package com.sprint.mission.discodeit.controller.advice;

import com.sprint.mission.discodeit.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.error("오류 발생: " + e.getMessage()));
    }
}
