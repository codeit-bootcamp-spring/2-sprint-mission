package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ResponseErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FileExceptionHandler {

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

  @ExceptionHandler(DuplicateFilePathException.class)
  public ResponseEntity<ResponseErrorBody> handleDuplicateFilePathException(
      DuplicateFilePathException e) {
    log.warn("DuplicateFilePathException handled: {}, details: {}", e.getMessage(), e.getDetails());
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(FileReadException.class)
  public ResponseEntity<ResponseErrorBody> handleFileReadException(FileReadException e) {
    log.error("FileReadException handled: {}, details: {}", e.getMessage(), e.getDetails(), e);
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(FileDeleteException.class)
  public ResponseEntity<ResponseErrorBody> handleFileDeleteException(FileDeleteException e) {
    log.error("FileDeleteException handled: {}, details: {}", e.getMessage(), e.getDetails(), e);
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(FileWriterException.class)
  public ResponseEntity<ResponseErrorBody> handleFileWriteException(FileWriterException e) {
    log.error("FileWriteException handled: {}, details: {}", e.getMessage(), e.getDetails(), e);
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(InitDirectoryException.class)
  public ResponseEntity<ResponseErrorBody> handleInitDirectoryException(InitDirectoryException e) {
    log.error("InitDirectoryException handled: {}, details: {}", e.getMessage(), e.getDetails(), e);
    return ResponseEntity.status(e.getResultCode().getStatus()).body(new ResponseErrorBody(e));
  }
}
