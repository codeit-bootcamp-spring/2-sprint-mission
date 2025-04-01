package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.dto.BaseResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponseDto> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(BaseResponseDto.failure(e.getMessage()));
    }
}
