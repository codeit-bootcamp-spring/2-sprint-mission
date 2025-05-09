package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.file.FileDeleteException;
import com.sprint.mission.discodeit.exception.file.FileReadException;
import com.sprint.mission.discodeit.exception.file.ProfileFileTypeException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
// Controller 공통 예외를 처리하는 핸들러 (중복 코드 감소)
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseErrorBody> handleBodyValidException(
      MethodArgumentNotValidException e) {
    log.warn("MethodArgumentNotValidException handled: {}", e.getMessage());
    return ResponseEntity.badRequest().body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseErrorBody> handleSingleValidException(
      ConstraintViolationException e) {
    log.warn("ConstraintViolationException handled: {}", e.getMessage());
    return ResponseEntity.badRequest().body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ResponseErrorBody> handleMaxSizeException(
      MaxUploadSizeExceededException e) {
    log.warn("MaxUploadSizeExceededException handled: {}", e.getMessage());
    return ResponseEntity.badRequest().body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(ProfileFileTypeException.class)
  public ResponseEntity<ResponseErrorBody> handleProfileFileException(
      ProfileFileTypeException e) {
    log.warn("ProfileFileContentTypeException handled: {}, details: {}", e.getMessage(),
        e.getDetails());
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(FileReadException.class)
  public ResponseEntity<ResponseErrorBody> handleFileReadException(FileReadException e) {
    log.error("FileReadException handled: {}, details: {}", e.getMessage(), e.getDetails(), e);
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }


  // 예상하지 못한 예외만 error 로그 남김 (+ stack trace)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseErrorBody> handleUnexpectedException(Exception e) {
    log.error("User unexpectedException handled: ", e);
    return ResponseEntity.internalServerError().body(new ResponseErrorBody(e));
  }

}
