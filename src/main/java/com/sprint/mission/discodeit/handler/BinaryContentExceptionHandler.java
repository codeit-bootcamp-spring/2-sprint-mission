package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.exception.binarycontent.FileIdNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BinaryContentExceptionHandler {
    @ExceptionHandler(FileIdNotFoundException.class)
    public String handleFileIdNotFoundException(FileIdNotFoundException e) {
        return e.getMessage();
    }
}
