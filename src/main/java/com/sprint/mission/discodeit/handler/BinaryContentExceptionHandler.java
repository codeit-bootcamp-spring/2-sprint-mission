package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.exception.binarycontent.FileIdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BinaryContentExceptionHandler {
    @ExceptionHandler(FileIdNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileIdNotFoundException(FileIdNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, e.getMessage(), null));
    }
}
