package com.sprint.mission.discodeit.exception.s3;

import com.sprint.mission.discodeit.exception.ResponseErrorBody;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@RestControllerAdvice
public class S3ExceptionHandler {

  @ExceptionHandler(S3UploadException.class)
  public ResponseEntity<ResponseErrorBody> handleS3UploadException(S3UploadException e) {
    log.error("s3UploadException handled: {}, details: {}", e.getMessage(), e.getDetails(), e);
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(S3DownloadException.class)
  public ResponseEntity<ResponseErrorBody> handleS3DownloadException(S3DownloadException e) {
    log.error("s3DownloadException handled: {}, details: {}", e.getMessage(), e.getDetails(), e);
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(S3DeleteException.class)
  public ResponseEntity<ResponseErrorBody> handleS3DeleteException(S3DeleteException e) {
    log.error("s3DeleteException handled: {}, details: {}", e.getMessage(), e.getDetails(), e);
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(S3Exception.class)
  public ResponseEntity<ResponseErrorBody> handleUnexpectedS3Exception(S3Exception e) {
    log.error("unexpectedS3Exception handled: ", e);
    ResponseErrorBody errorBody = new ResponseErrorBody(
        Instant.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.name(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Unexcepected s3 error occurred",
        Map.of(),
        e.getClass().getSimpleName());
    return ResponseEntity.internalServerError().body(errorBody);
  }
}
