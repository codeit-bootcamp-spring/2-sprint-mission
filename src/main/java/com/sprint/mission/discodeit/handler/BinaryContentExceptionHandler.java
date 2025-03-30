package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.exception.binarycontent.FileFindException;
import com.sprint.mission.discodeit.exception.binarycontent.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BinaryContentExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleFileUploadExceptionException(FileUploadException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(FileFindException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileFindException(FileFindException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ApiResponse<>(false, e.getMessage(), null));
    }
}
