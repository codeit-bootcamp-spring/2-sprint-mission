package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.custom.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.custom.UnauthorizedAccessException;
import java.time.Instant;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResult> resourceNotFoundExHandle(ResourceNotFoundException e) {
        log.error("[exceptionHandle ex", e);
        Instant now = Instant.now();
        ErrorResult errorResult =
            new ErrorResult(now, "RESOURCE-NOT-FOUND", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResult> duplicateResourceExHandle(DuplicateResourceException e) {
        log.error("exceptionHandle ex", e);
        Instant now = Instant.now();
        ErrorResult errorResult =
            new ErrorResult(now, "DUPLICATE-RESOURCE", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResult> unauthorizedExHandle(UnauthorizedAccessException e) {
        log.error("[exceptionHandler ex", e);
        Instant now = Instant.now();
        ErrorResult errorResult =
            new ErrorResult(now, "UNAUTHORIZED-ACCESS", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        Instant now = Instant.now();
        ErrorResult errorResult =
            new ErrorResult(now, "BAD-REQUEST", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResult> noSuchExHandle(NoSuchElementException e) {
        log.error("[exceptionHandle] ex", e);
        Instant now = Instant.now();
        ErrorResult errorResult =
            new ErrorResult(now, "NO-SUCH-ELEMENT", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        Instant now = Instant.now();
        ErrorResult errorResult =
            new ErrorResult(now, "EXCEPTION", "서버 내부 오류");

        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
